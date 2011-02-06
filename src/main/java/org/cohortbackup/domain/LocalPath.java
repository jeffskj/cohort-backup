package org.cohortbackup.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.collect.Sets;

@Entity
public class LocalPath {
    @Id
    @GeneratedValue
    private long id;

    private int priority = -1;
    private File file;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BackupItem> backupItems;

    @ManyToOne(cascade = CascadeType.ALL)
    private LocalPath parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<LocalPath> children;

    public LocalPath(File path, LocalPath parent) {
        file = path;
        this.parent = parent;
    }

    public LocalPath(File path) {
        this(path, null);
    }

    public LocalPath() {
    }

    /**
     * refreshes the list of direct child paths (folders or files)
     */
    public void refreshChildren() {
        if (!file.isDirectory()) {
            return;
        }

        Set<File> knownChildren = new HashSet<File>();
        for (LocalPath child : getChildren()) {
            knownChildren.add(child.getFile());
        }

        Set<File> actualChildren = Sets.newHashSet(file.listFiles());
        actualChildren.removeAll(knownChildren);
        
        for (File file : actualChildren) {
            getChildren().add(new LocalPath(file, this));
        }

        for (LocalPath child : getChildren()) {
            child.refreshChildren();
        }
    }

    public boolean isDeleted() {
        return !file.exists();
    }

    public boolean isBackedUp() {
        return backupItems != null && !backupItems.isEmpty()
                && backupItems.get(backupItems.size() - 1).getBackupDate() != null;
    }
    
    public BackupItem getBackupItemAwaitingSend() {
        if (backupItems == null) { return null; }
        
        for (BackupItem bi : backupItems) {
            if (bi.getBackupDate() == null) {
                return bi;
            }
        }
        
        return null;        
    }

    public List<BackupItem> getBackupItems() {
        return backupItems;
    }

    public void setBackupItems(List<BackupItem> backupItems) {
        this.backupItems = backupItems;
    }

    public LocalPath getParent() {
        return parent;
    }

    public void setParent(LocalPath parent) {
        this.parent = parent;
    }

    public LocalPath getChild(String name) {
        for (LocalPath child : children) {
            if (child.file.getName().equals(name)) {
                return child;
            }
        }

        return null;
    }

    public List<LocalPath> getChildren() {
        if (children == null) {
            children = new ArrayList<LocalPath>();
        }
        return children;
    }

    public void setChildren(List<LocalPath> children) {
        this.children = children;
    }

    public void setFile(File path) {
        file = path;
    }

    public File getFile() {
        return file;
    }

    public boolean isOutOfDate() {
        if (!isBackedUp()) {
            return true;
        }

        Date lastBackup = backupItems.get(backupItems.size() - 1).getBackupDate();
        return lastBackup == null || lastBackup.before(new Date(file.lastModified()));
    }

    protected void setId(long id) {
        this.id = id;
    }

    protected long getId() {
        return id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        if (priority == -1 && parent != null) {
            return parent.getPriority(); // inherited from parent
        }
        return priority;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(file).toString();
    }
}