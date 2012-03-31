package org.cohortbackup.backup;

import java.util.List;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;

public class BackupSendServiceImpl implements BackupSendService {

    @Override
    public void sendBackups(LocalRepository repos) {
    	List<Path> unsentPaths = repos.getIndex().getUnsentPaths();
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
				backupLocation.getBackupClient().send(item, repos.get(item.getId()));
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
