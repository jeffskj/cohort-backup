package org.cohort.repos;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="backup-log")
public class BackupLog {
    
    @XmlElement(name="entry")
    private List<BackupLogEntry> logEntries = Lists.newArrayList();
    
    @XmlTransient
    private Multimap<UUID, BackupLogEntry> itemBackups = HashMultimap.create(); 
    
    public void addEntry(BackupItem item, BackupLocation location) {
        BackupLogEntry entry = new BackupLogEntry(item.getId(), location.getId(), new Date());
        logEntries.add(entry);
        itemBackups.put(item.getId(), entry);
    }
    
    public List<BackupLogEntry> getLogEntries() {
        return Collections.unmodifiableList(logEntries);
    }
    
    public boolean isUnsent(BackupItem item) {
    	return itemBackups.containsKey(item.getId());
    }
    
    @Deprecated 
    /**
     * not actually deprecated, just wanted to hide it from type completion
     */
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        for (BackupLogEntry e : logEntries) {
            itemBackups.put(e.getBackupItemId(), e);
        }
    }
 
}
