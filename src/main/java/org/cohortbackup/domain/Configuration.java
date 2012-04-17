package org.cohortbackup.domain;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang.RandomStringUtils;
import org.cohortbackup.backup.FolderBackupLocation;
import org.cohortbackup.backup.SkydriveBackupLocation;

import com.google.common.collect.Lists;

//TODO: refactor to have file access encapsulated in this object, it probably makes more sense
// similar to ActiveRecord here, .save(), .load(), getFile(), getRaw(), etc
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
	private UUID selfId;
    private String encryptionKey;
    private String secretPasword;
    
    @XmlElementWrapper
    @XmlElements({
    	@XmlElement(name="skydrive", type=SkydriveBackupLocation.class),
    	@XmlElement(name="folder", type=FolderBackupLocation.class),
    })
    private List<BackupLocation> backupLocations = Lists.newArrayList();
    
    public Configuration() {}
    public Configuration(UUID selfId) {
    	this.selfId = selfId;
    }
    
    public void generateEncryptionKey() {
    	if (encryptionKey == null) {
    		encryptionKey = RandomStringUtils.randomAscii(50); 
    	}
    }
    
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

	public String getSecretPasword() {
		return secretPasword;
	}

	public void setSecretPasword(String secretPasword) {
		this.secretPasword = secretPasword;
	}

	public UUID getSelfId() {
		return selfId;
	}

	public void setSelfId(UUID selfId) {
		this.selfId = selfId;
	}
}