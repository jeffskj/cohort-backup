package org.cohortbackup.backup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.cohort.repos.BackupLogEntry;
import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.encryption.EncryptionService;

public class RemoteBackupService {

    @Inject
    EncryptionService encryptionService;
	
    public void sendBackups(LocalRepository repos) {
    	List<Path> unsentPaths = repos.getIndex().getUnsentPaths(repos.getBackupLog());
    	try {
	        for (Path p : unsentPaths) {
	        	for (BackupItem item : p.getBackupItems()) {
	        		if (repos.getBackupLog().isUnsent(item)) {
	        			sendBackup(repos, item);
	        			repos.saveBackupLog();
	        		}
	        	}
	        }
	        
	        Configuration config = repos.getConfig();
			sendBackup(config, ".config", encryptionService.encrypt(repos.getRawConfig(), config.getSecretPasword()));
			sendBackup(config, ".backuplog", encryptionService.encrypt(repos.getRawBackupLog(), config.getEncryptionKey()));
			sendBackup(config, ".index", encryptionService.encrypt(repos.getRawIndex(), config.getEncryptionKey()));
			repos.saveBackupLog();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    public InputStream recover(LocalRepository repos, BackupItem item) {
    	Collection<BackupLogEntry> logEntries = repos.getBackupLog().getLogEntries(item);
    	
    	for (BackupLogEntry entry : logEntries) {
    		BackupLocation location = repos.getConfig().getBackupLocation(entry.getBackupLocationId());
    		return location.getBackupClient().receive(item.getId().toString());
    	}
    	
    	return null;
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
