package org.cohort.repos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.cohortbackup.domain.BackupItem;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@XmlRootElement(name="path")
@XmlAccessorType(XmlAccessType.FIELD)
public class Path {
    private int priority = -1;
    
    @XmlTransient
    private Path parent;
    
    @XmlJavaTypeAdapter(PathXmlAdapter.class)
    private File file;

    @XmlElement(name="backup")
    private LinkedList<BackupItem> backupItems = Lists.newLinkedList();
    
    @XmlElement(name="path")
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
        return !backupItems.isEmpty();
    }

    public List<BackupItem> getBackupItems() {
        return backupItems;
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

    public boolean isFile() {
        return file.isFile();
    }
    
    public void setFile(File path) {
        file = path;
    }

    public File getFile() {
        return file;
    }
    
    public boolean isAwaitingSend() {
        return backupItems.isEmpty() || backupItems.peekLast().getBackupDate() == null;
    }

    public boolean isOutOfDate() {
        if (!isBackedUp()) {
            return true;
        }

        Date lastBackup = backupItems.peekLast().getBackupDate();
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

    public InputStream openStream() throws IOException {
        return new BufferedInputStream(FileUtils.openInputStream(file));
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append(file).toString();
    }
    
    @Deprecated 
    /**
     * not actually deprecated, just wanted to hide it from type completion
     */
    public void afterUnmarshal(Unmarshaller u, Object parent) {
        if (parent instanceof Path) {
            this.parent = (Path) parent;
        }
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