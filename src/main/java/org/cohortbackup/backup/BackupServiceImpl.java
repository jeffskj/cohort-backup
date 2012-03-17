package org.cohortbackup.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;

import org.cohort.repos.Index;
import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.encryption.EncryptionService;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackupServiceImpl implements BackupService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    EncryptionService encryptionService;

    @Inject
    Index localIndex;

    @Inject
    @Current
    Node currentNode;

    @Inject
    @Current
    Swarm currentSwarm;

    @Override
    public void backup(LocalRepository repos) {
        for (Path path : localIndex.getOutOfDatePaths()) {
            BackupItem awaitingSend = path.getBackupItemAwaitingSend();
            BackupItem backupItem = awaitingSend != null ? awaitingSend : createBackupItem(path);
        }
    }

    @Override
    public void recover(LocalRepository repos, BackupItem backupItem) {
    }

    public BackupItem createBackupItem(Path path) {
        BackupItem backup = new BackupItem();
        backup.setId(UUID.randomUUID());
        backup.setSize(path.getFile().length());
        return backup;
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
