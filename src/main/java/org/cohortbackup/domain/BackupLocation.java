package org.cohortbackup.domain;

import java.util.UUID;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.cohortbackup.backup.SkydriveBackupLocation;

@XmlSeeAlso({SkydriveBackupLocation.class})
public interface BackupLocation {
    UUID getId();
    BackupClient getBackupClient(); 
}
