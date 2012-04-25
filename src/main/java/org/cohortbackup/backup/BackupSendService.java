package org.cohortbackup.backup;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.encryption.EncryptionService;

public class BackupSendService {

    @Inject
    EncryptionService encryptionService;
	
    public void sendBackups(LocalRepository repos) {
    	List<Path> unsentPaths = repos.getIndex().getUnsentPaths(repos.getBackupLog());
        for (Path p : unsentPaths) {
        	for (BackupItem item : p.getBackupItems()) {
        		if (repos.getBackupLog().isUnsent(item)) {
        			sendBackup(repos, item);
        		}
        	}
        }
        
        Configuration config = repos.getConfig();
		try {
			sendBackup(config, ".config", encryptionService.encrypt(repos.getRawConfig(), config.getSecretPasword()));
			sendBackup(config, ".backuplog", encryptionService.encrypt(repos.getRawBackupLog(), config.getEncryptionKey()));
			sendBackup(config, ".index", encryptionService.encrypt(repos.getRawIndex(), config.getEncryptionKey()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	private void sendBackup(LocalRepository repos, BackupItem item) {
		for (BackupLocation backupLocation : repos.getConfig().getBackupLocations()) {
			try {
				backupLocation.getBackupClient().send(item.getId().toString(), repos.get(item.getId()));
				return;
			} catch (Exception e) {
			    throw new BackupException(item, backupLocation);
			}
		}
	}

	private void sendBackup(Configuration config, String subPath, InputStream stream) {
		for (BackupLocation backupLocation : config.getBackupLocations()) {
			try {
				backupLocation.getBackupClient().send(config.getSelfId().toString() + "/" + subPath, stream);
				return;
			} catch (Exception e) {
			    throw new BackupException(subPath, backupLocation);
			}
		}
	}
}
