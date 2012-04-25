package org.cohortbackup.backup;

import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;

public class BackupException extends RuntimeException {
    private static final long serialVersionUID = -1850995895738449678L;
    
    public BackupException(BackupItem item, BackupLocation backupLocation) {
        super("couldn't backup item " + item.getId() + " to location " + backupLocation.getId());
    }

	public BackupException(String path, BackupLocation backupLocation) {
		super("couldn't path " + path + " to location " + backupLocation.getId());
	}
}
