package org.cohortbackup.backup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.inject.Inject;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.encryption.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * creates/recovers local backups from a local repository 
 *
 */
public class BackupServiceImpl implements BackupService {
    private static final int BUFFER_SIZE = 4096;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    EncryptionService encryptionService;

    @Override
    public void backup(LocalRepository repos) throws IOException {
        for (Path path : repos.getIndex().getOutOfDatePaths()) {
            BackupItem item = createBackupItem(path);
            logger.info("creating backup item {} for path {}", item.getId(), path);
            path.getBackupItems().add(item);
            repos.put(item.getId(), encryptionService.encrypt(gzip(path.openStream()), repos.getConfig().getEncryptionKey()));
        }
        
        // backup metadata
        // encrypt config file with user supplied password, also stored in config, not encrypted locally
        // encrypt all other metadata files with generated key
    }

    @Override
    public InputStream recover(LocalRepository repos, BackupItem backupItem) throws IOException {
            return unzip(encryptionService.decrypt(repos.get(backupItem.getId()), repos.getConfig().getEncryptionKey()));
    }

    private InputStream unzip(InputStream is) throws IOException {
        return new InflaterInputStream(is, new Inflater(), BUFFER_SIZE);
    }
    
    private InputStream gzip(InputStream is) throws FileNotFoundException, IOException {
        return new DeflaterInputStream(is, new Deflater(9), BUFFER_SIZE);
    }
    
    private BackupItem createBackupItem(Path path) {
        BackupItem backup = new BackupItem();
        backup.setId(UUID.randomUUID());
        backup.setSize(path.getFile().length());
        backup.setVersion(path.getBackupItems().size()+1);
        backup.setBackupDate(new Date());
        return backup;
    }
}