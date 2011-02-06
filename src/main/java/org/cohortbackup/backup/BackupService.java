package org.cohortbackup.backup;

import org.cohortbackup.domain.BackupItem;

public interface BackupService
{
    void backup();
    void recover(BackupItem backupItem);
}