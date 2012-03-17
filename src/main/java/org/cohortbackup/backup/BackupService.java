package org.cohortbackup.backup;

import java.io.IOException;
import java.io.InputStream;

import org.cohort.repos.LocalRepository;
import org.cohortbackup.domain.BackupItem;

public interface BackupService {
    void backup(LocalRepository repos) throws IOException;
    InputStream recover(LocalRepository repos, BackupItem backupItem) throws IOException;
}