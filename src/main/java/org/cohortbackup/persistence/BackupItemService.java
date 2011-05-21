package org.cohortbackup.persistence;

import java.util.UUID;

import org.cohortbackup.domain.BackupItem;
import org.jboss.seam.transaction.Transactional;

@Transactional
public interface BackupItemService {
    BackupItem getBackupItem(UUID id);
    void save(BackupItem item);
}
