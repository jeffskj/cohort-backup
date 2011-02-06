package org.cohortbackup.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * tree like data structure that acts like a view into a file-system of backed up items.  
 * maps paths -> backup item 
 * @author Skjonsby
 */
public class RemotePath
{
    private String name;
    private List<BackupItem> backupItems = new LinkedList<BackupItem>();
    private RemotePath parent;
    private Map<String, RemotePath> children = new HashMap<String, RemotePath>();

    public RemotePath(String name)
    {
        this.name = name;
    }
    
    public RemotePath()
    {
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<BackupItem> getBackupItems()
    {
        return backupItems;
    }

    public RemotePath getParent()
    {
        return parent;
    }

    public Set<RemotePath> getChildren()
    {
        return new HashSet<RemotePath>(children.values());
    }

    public RemotePath getChild(String name)
    {
        return children.get(name);
    }
    
    private RemotePath getOrCreateChild(String name)
    {
        if (!children.containsKey(name))
        {
            RemotePath path = new RemotePath(name);
            path.parent = this;
            children.put(name, path);
        }
        return children.get(name);
    }

    public void addBackupItem(String path, BackupItem backupItem)
    {
        if (path.isEmpty()) 
        {
            backupItems.add(backupItem);
        }
        else 
        {
            String name = StringUtils.substringBefore(path, "/");
            RemotePath child = getOrCreateChild(name);
            path = path.substring(Math.min(name.length() + 1, path.length()));
            child.addBackupItem(path, backupItem);
        }
    }
}
