package org.cohortbackup.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Node
{
    @Id 
    private UUID uuid;
    private String ipAddress;
    private String name;
    private long totalSpace;
    private long freeSpace;
    private int connectionSpeed;
    
    @XmlTransient
    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private Swarm swarm;
    
    @XmlTransient
    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="backupNodes")
    private Set<BackupItem> backupItems = new HashSet<BackupItem>();    
    
    public Node()
    {
    }

    public Node(UUID id)
    {
        uuid = id;
    }
    
    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getTotalSpace()
    {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace)
    {
        this.totalSpace = totalSpace;
    }

    public long getFreeSpace()
    {
        return freeSpace;
    }

    public void setFreeSpace(long freeSpace)
    {
        this.freeSpace = freeSpace;
    }

    public int getConnectionSpeed()
    {
        return connectionSpeed;
    }

    public void setConnectionSpeed(int connectionSpeed)
    {
        this.connectionSpeed = connectionSpeed;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public void setBackupItems(Set<BackupItem> backupItems)
    {
        this.backupItems = backupItems;
    }

    public Set<BackupItem> getBackupItems()
    {
        return backupItems;
    }
    
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public void setSwarm(Swarm swarm)
    {
        this.swarm = swarm;
    }

    public Swarm getSwarm()
    {
        return swarm;
    }
}
