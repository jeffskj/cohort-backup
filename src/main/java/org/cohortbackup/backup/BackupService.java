package org.cohortbackup.backup;

import org.cohort.repos.LocalRepository;
import org.cohortbackup.domain.BackupItem;

public interface BackupService {
    void backup(LocalRepository repos);
    void recover(LocalRepository repos, BackupItem backupItem);
}