package org.cohortbackup.remoting;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Node;

public interface BackupItemWebServiceClient {
    BackupItem getBackupItem(Node from, UUID backupItemId);
    InputStream getContents(Node from, UUID id) throws IOException;
    void sendBackupItem(Node to, BackupItem item);
    boolean sendContents(Node to, BackupItem item);
}