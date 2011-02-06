package org.cohortbackup.backup;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Node;
import org.cohortbackup.remoting.BackupItemWebServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockBackupItemWebServiceClient implements BackupItemWebServiceClient {
    Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public BackupItem getBackupItem(Node from, UUID backupItemId) {
        return null;
    }

    @Override
    public InputStream getContents(Node from, UUID id) throws IOException {
        return null;
    }

    @Override
    public void sendBackupItem(Node to, BackupItem item) {
        logger.info("sending {} to {}", item.getId(), to.getIpAddress());
    }

    @Override
    public boolean sendContents(Node to, BackupItem item) {
        logger.info("sending contents {} to {}", item.getId(), to.getIpAddress());
        return true;
    }

}
