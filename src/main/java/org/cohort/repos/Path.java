package org.cohort.repos;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.cohortbackup.domain.BackupItem;

import com.google.common.collect.Sets;

@XmlAccessorType(XmlAccessType.FIELD)
public class Path {
    private int priority = -1;
    
    @XmlJavaTypeAdapter(PathXmlAdapter.class)
    private File file;

    private List<BackupItem> backupItems;
    private Path parent;
    private List<Path> children;

    public Path(File path, Path parent) {
        file = path;
        this.parent = parent;
    }

    public Path(File path) {
        this(path, null);
    }

    public Path() {
    }

    /**
     * refreshes the list of direct child paths (folders or files)
     */
    public void refreshChildren() {
        if (!file.isDirectory()) {
            return;
        }

        Set<File> knownChildren = new HashSet<File>();
        for (Path child : getChildren()) {
            knownChildren.add(child.getFile());
        }

        Set<File> actualChildren = Sets.newHashSet(file.listFiles());
        actualChildren.removeAll(knownChildren);

        for (File file : actualChildren) {
            getChildren().add(new Path(file, this));
        }

        for (Path child : getChildren()) {
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
        if (backupItems == null) {
            return null;
        }

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

    public Path getParent() {
        return parent;
    }

    public void setParent(Path parent) {
        this.parent = parent;
    }

    public Path getChild(String name) {
        for (Path child : children) {
            if (child.file.getName().equals(name)) {
                return child;
            }
        }

        return null;
    }

    public List<Path> getChildren() {
        if (children == null) {
            children = new ArrayList<Path>();
        }
        return children;
    }

    public void setChildren(List<Path> children) {
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
    
    private static class PathXmlAdapter extends XmlAdapter<String, File> {
        @Override
        public File unmarshal(String v) throws Exception {
            return new File(v);
        }

        @Override
        public String marshal(File v) throws Exception {
            return v.getAbsolutePath();
        }        
    }
}