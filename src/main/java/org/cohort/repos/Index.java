package org.cohort.repos;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

@Alternative
@XmlRootElement
public class Index {
    
    @XmlElement(name="path")
    private List<Path> roots = Lists.newArrayList();

    public Index() { // to placate weld
    }

    public Index(List<Path> roots) {
        this.roots = roots;
    }

    public List<Path> getOutOfDatePaths() {
        return depthFirstSearch(new Predicate<Path>() {
            @Override
            public boolean apply(Path path) {
                return path.isOutOfDate() && path.getFile().isFile();
            }
        });
    }

    public List<Path> getDeletedPaths() {
        return depthFirstSearch(new Predicate<Path>() {
            @Override
            public boolean apply(Path path) {
                return path.isDeleted() && path.isBackedUp();
            }
        });
    }

    private List<Path> depthFirstSearch(Predicate<Path> pred) {
        List<Path> matching = new LinkedList<Path>();
        for (Path path : roots) {
            matching.addAll(depthFirstSearch(pred, path));
        }
        return matching;
    }

    private List<Path> depthFirstSearch(Predicate<Path> pred, Path root) {
        List<Path> matching = new LinkedList<Path>();
        root.refreshChildren();
        for (Path path : root.getChildren()) {
            if (pred.apply(path)) {
                matching.add(path);
            }
            matching.addAll(depthFirstSearch(pred, path));
        }
        return matching;
    }

    public List<Path> getRoots() {
        return roots;
    }

    public void addRoot(Path path) {
        path.refreshChildren();
        roots.add(path);
    }
}