package org.cohort.repos;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BackupLogEntry {
    
    @XmlElement(name="item")
    private UUID backupItemId;
    
    @XmlElement(name="location")
    private UUID backupLocationId;
    
    private Date timestamp;

    public BackupLogEntry() {
    }

    public BackupLogEntry(UUID backupItemId, UUID backupLocationId, Date timestamp) {
        this.backupItemId = backupItemId;
        this.backupLocationId = backupLocationId;
        this.timestamp = timestamp;
    }

    public UUID getBackupItemId() {
        return backupItemId;
    }

    public void setBackupItemId(UUID backupItemId) {
        this.backupItemId = backupItemId;
    }

    public UUID getBackupLocationId() {
        return backupLocationId;
    }

    public void setBackupLocationId(UUID backupLocationId) {
        this.backupLocationId = backupLocationId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
