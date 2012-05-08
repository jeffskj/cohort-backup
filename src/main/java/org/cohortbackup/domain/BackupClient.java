package org.cohortbackup.domain;

import java.io.InputStream;

public interface BackupClient {
    void send(String path, InputStream input);
    InputStream receive(String path);
}
