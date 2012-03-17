package org.cohortbackup.domain;

import java.io.InputStream;

public interface BackupClient {
    void send(BackupItem item, InputStream input);
}
