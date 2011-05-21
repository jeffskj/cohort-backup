package org.cohortbackup.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BackupItem {
    @Id //@Type(type="uuid-char")
    private UUID id;
    private String encryptedPath;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date backupDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Node originNode;

    @XmlElementWrapper(name = "backupNodes")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Node> backupNodes = new HashSet<Node>();
    private int version;
    private long size; // size in bytes

    public String getEncryptedPath() {
        return encryptedPath;
    }

    public void setEncryptedPath(String path) {
        encryptedPath = path;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        id = uuid;
    }

    public Date getBackupDate() {
        return backupDate;
    }

    public void setBackupDate(Date lastBackup) {
        backupDate = lastBackup;
    }

    public Node getOriginNode() {
        return originNode;
    }

    public void setOriginNode(Node originNode) {
        this.originNode = originNode;
    }

    public Set<Node> getBackupNodes() {
        return backupNodes;
    }

    public void setBackupNodes(Set<Node> backupNodes) {
        this.backupNodes = backupNodes;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}