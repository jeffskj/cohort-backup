package org.cohortbackup.domain;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Alternative;

import com.google.common.base.Predicate;

@Alternative
public class LocalIndex {
    private List<LocalPath> backupRoots;
    
    public LocalIndex() { // to placate weld
    }
    
    public LocalIndex(List<LocalPath> roots) {
        backupRoots = roots;
    }
    
    public List<LocalPath> getOutOfDatePaths() {
        return depthFirstSearch(new Predicate<LocalPath>() {
            @Override
            public boolean apply(LocalPath path) { 
                return path.isOutOfDate() && path.getFile().isFile(); 
            }
        });
    }
    
    public List<LocalPath> getDeletedPaths() {
        return depthFirstSearch(new Predicate<LocalPath>() {
            @Override
            public boolean apply(LocalPath path) { 
                return path.isDeleted() && path.isBackedUp(); 
            }
        });
    }

    private List<LocalPath> depthFirstSearch(Predicate<LocalPath> pred) {
        List<LocalPath> matching = new LinkedList<LocalPath>();
        for (LocalPath path : backupRoots) {
            matching.addAll(depthFirstSearch(pred, path));
        }
        return matching;
    }

    private List<LocalPath> depthFirstSearch(Predicate<LocalPath> pred, LocalPath root) {
        List<LocalPath> matching = new LinkedList<LocalPath>();
        root.refreshChildren();
        for (LocalPath path : root.getChildren()) {
            if (pred.apply(path)) {
                matching.add(path);
            }
            matching.addAll(depthFirstSearch(pred, path));
        }
        return matching;
    }

    public List<LocalPath> getBackupRoots() {
        return backupRoots;
    }
}