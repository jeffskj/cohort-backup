package org.cohortbackup.backup;

import java.io.IOException;

import org.cohort.repos.LocalRepository;
import org.cohortbackup.domain.BackupItem;

public interface BackupService {
    void backup(LocalRepository repos) throws IOException;
    void recover(LocalRepository repos, BackupItem backupItem);
}