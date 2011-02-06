package org.cohortbackup.persistence;

import java.util.UUID;

import org.cohortbackup.domain.BackupItem;

public interface BackupItemService {
    BackupItem getBackupItem(UUID id);
    void save(BackupItem item);
}
