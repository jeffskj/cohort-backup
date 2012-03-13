package org.cohortbackup.domain;

import java.io.File;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Configuration {
    public static final long ID = 1L;

    @Id
    @SuppressWarnings("unused")
    private long id = ID;

    private File localRepositoryDir;
    private String encryptionKey;
    private UUID swarmId;
    private UUID nodeId;

    public void setLocalRepositoryDir(File localRepositoryPath) {
        localRepositoryDir = localRepositoryPath;
    }

    public File getLocalRepositoryDir() {
        return localRepositoryDir;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setSwarmId(UUID swarmId) {
        this.swarmId = swarmId;
    }

    public UUID getSwarmId() {
        return swarmId;
    }

    public void setNodeId(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public UUID getNodeId() {
        return nodeId;
    }
}
