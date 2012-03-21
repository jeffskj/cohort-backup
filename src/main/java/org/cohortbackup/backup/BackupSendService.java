package org.cohortbackup.backup;

import org.cohort.repos.LocalRepository;

public interface BackupSendService {
    void sendBackups(LocalRepository repos);
}
