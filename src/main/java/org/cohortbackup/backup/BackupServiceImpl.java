package org.cohortbackup.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.LocalIndex;
import org.cohortbackup.domain.LocalPath;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.encryption.EncryptionService;
import org.cohortbackup.remoting.BackupItemWebServiceClient;
import org.h2.util.IOUtils;
import org.jboss.seam.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class BackupServiceImpl implements BackupService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Inject
    EncryptionService encryptionService;

    @Inject @Current
    Configuration config;

    @Inject
    LocalRepository repository;

    @Inject
    LocalIndex localIndex;
    
    @Inject @Current
    Node currentNode;

    @Inject @Current
    Swarm currentSwarm;

    @Inject
    BackupItemWebServiceClient backupClient;
    
    @Override
    public void backup() {
        for (LocalPath path : localIndex.getOutOfDatePaths()) {
            BackupItem awaitingSend = path.getBackupItemAwaitingSend();
            BackupItem backupItem = awaitingSend != null ? awaitingSend : createBackupItem(path);
            
            Node node = findEligibleNode();
            if (node == null) {
                logger.info("skipping backup of {} because there were no eligible nodes!", path);
                continue;
            }
            try {
                backupClient.sendBackupItem(node, backupItem);
                if (backupClient.sendContents(node, backupItem)) {
                    backupItem.getBackupNodes().add(node);
                    backupItem.setBackupDate(new Date());
                    backupClient.sendBackupItem(node, backupItem);
                } else {
                    logger.warn("sending contents of {} failed, skipping for now...", path);
                }
            } catch (RuntimeException e) {
                logger.warn("skipping backup of {} because of error!", path, e);
                continue;
            }
            
        }
    }

    private Node findEligibleNode() {
        for (Node n : currentSwarm.getNodes()) {
            if (!n.getId().equals(currentNode.getId())) {
                return n;
            }
        }
        return null;
    }

    @Override
    public void recover(BackupItem backupItem) {
    }

    public BackupItem createBackupItem(LocalPath path) {
        BackupItem backup = new BackupItem();
        backup.setId(UUID.randomUUID());
        backup.setSize(path.getFile().length());
        backup.setOriginNode(currentNode);
        String normalizedPath = FilenameUtils.separatorsToUnix(path.getFile().getAbsolutePath());
        backup.setEncryptedPath(encryptionService.encrypt(normalizedPath, config.getEncryptionKey()));

        try {
            repository.put(backup.getId(), encrypt(new FileInputStream(path.getFile())));
            return backup;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    InputStream encrypt(InputStream in) {
        return encryptionService.encrypt(in, config.getEncryptionKey());
    }
    
    public void recover(BackupItem backupItem, File destination, String key) {
        try {
            InputStream localFile = repository.get(backupItem.getId());
            GZIPInputStream in = new GZIPInputStream(encryptionService.decrypt(localFile, config.getEncryptionKey()));
            IOUtils.copy(in, new FileOutputStream(destination));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
