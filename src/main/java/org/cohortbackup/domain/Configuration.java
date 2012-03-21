package org.cohortbackup.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.cohortbackup.backup.SkydriveBackupLocation;

import com.google.common.collect.Lists;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({SkydriveBackupLocation.class})
public class Configuration {
    private String encryptionKey;
    
    @XmlElementWrapper
    @XmlAnyElement(lax=true)
    private List<BackupLocation> backupLocations = Lists.newArrayList();
    
    
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public List<BackupLocation> getBackupLocations() {
        return backupLocations;
    }
    
    public void addBackupLocation(BackupLocation location) {
        backupLocations.add(location);
    }
}