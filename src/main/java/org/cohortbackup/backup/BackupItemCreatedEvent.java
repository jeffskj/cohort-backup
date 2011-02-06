package org.cohortbackup.backup;

import org.cohortbackup.domain.BackupItem;

public class BackupItemCreatedEvent {

    private final BackupItem backupItem;

    public BackupItemCreatedEvent(BackupItem backupItem) {
        this.backupItem = backupItem;
    }
    
    public BackupItem getBackupItem() {
        return backupItem;
    }
}
