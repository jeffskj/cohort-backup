package org.cohortbackup.domain;

import java.util.UUID;

public interface BackupLocation {
    UUID getId();
    BackupClient getBackupClient(); 
}
