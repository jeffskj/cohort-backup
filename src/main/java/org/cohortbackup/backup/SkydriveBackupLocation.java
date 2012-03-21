package org.cohortbackup.backup;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cohortbackup.domain.BackupClient;
import org.cohortbackup.domain.BackupLocation;

@XmlRootElement(name="skydrive")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkydriveBackupLocation implements BackupLocation {
    private UUID id;
    private String username;
    private String apiToken;
    
    @Override
    public BackupClient getBackupClient() {
        return null;
    }
    
    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}