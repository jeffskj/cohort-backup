package org.cohortbackup.backup;

import java.io.IOException;
import java.util.List;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;

public class BackupSendService {

    public void sendBackups(LocalRepository repos) {
    	List<Path> unsentPaths = repos.getIndex().getUnsentPaths(repos.getBackupLog());
        for (Path p : unsentPaths) {
        	for (BackupItem item : p.getBackupItems()) {
        		if (repos.getBackupLog().isUnsent(item)) {
        			sendBackup(repos, item);
        		}
        	}
        }
    }

	private void sendBackup(LocalRepository repos, BackupItem item) {
		for (BackupLocation backupLocation : repos.getConfig().getBackupLocations()) {
			try {
				backupLocation.getBackupClient().send(item.getId().toString(), repos.get(item.getId()));
				return;
			} catch (IOException e) {
			    throw new BackupException(item, backupLocation);
			}
		}
	}

}
