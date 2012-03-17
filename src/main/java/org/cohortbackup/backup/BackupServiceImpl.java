package org.cohortbackup.backup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.encryption.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * creates/recovers local backups from a local repository 
 * @author jeffskj@github.com
 *
 */
public class BackupServiceImpl implements BackupService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    EncryptionService encryptionService;

    @Override
    public void backup(LocalRepository repos) throws IOException {
        for (Path path : repos.getIndex().getOutOfDatePaths()) {
            BackupItem item = createBackupItem(path);
            logger.info("creating backup item {} for path {}", item.getId(), path);
            repos.put(item.getId(), encrypt(path.openStream(), repos.getConfig().getEncryptionKey()));
        }
    }

    @Override
    public void recover(LocalRepository repos, BackupItem backupItem) {
    }

    public BackupItem createBackupItem(Path path) {
        BackupItem backup = new BackupItem();
        backup.setId(UUID.randomUUID());
        backup.setSize(path.getFile().length());
        backup.setVersion(path.getBackupItems().size()+1);
        backup.setBackupDate(new Date());
        return backup;
    }

    InputStream encrypt(InputStream in, String key) {
        return encryptionService.encrypt(in, key);
    }

    public void recover(BackupItem backupItem, File destination, String key) {
//        try {
//            InputStream localFile = repository.get(backupItem.getId());
//            GZIPInputStream in = new GZIPInputStream(encryptionService.decrypt(localFile, config.getEncryptionKey()));
//            IOUtils.copy(in, new FileOutputStream(destination));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}
