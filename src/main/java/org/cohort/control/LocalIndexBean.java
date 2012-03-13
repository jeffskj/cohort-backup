package org.cohort.control;

import java.io.File;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.cohort.repos.Index;
import org.cohort.repos.Path;
import org.cohortbackup.persistence.LocalIndexService;
import org.jboss.seam.transaction.TransactionPropagation;
import org.jboss.seam.transaction.Transactional;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Model
public class LocalIndexBean {
    @Inject
    LocalIndexService localIndexService;

    @Inject
    Index localIndex;

    private File pathToAdd;
    private TreeNode root;

    @Transactional(TransactionPropagation.REQUIRED)
    public void addRootPath() {
        localIndexService.addRootPath(new Path(pathToAdd));
    }

    public List<Path> getBackupRoots() {
        return localIndexService.getLocalIndex().getRoots();
    }

    public TreeNode getRoot() {
        if (root == null) {
            root = new DefaultTreeNode("root", null);
            Index idx = localIndexService.getLocalIndex();
            idx.getOutOfDatePaths();

            for (Path p : idx.getRoots()) {
                root.addChild(new LocalPathTreeNode(p, root));
            }
        }
        return root;
    }

    public void setPathToAdd(File pathToAdd) {
        this.pathToAdd = pathToAdd;
    }

    public File getPathToAdd() {
        return pathToAdd;
    }

    public static class LocalPathTreeNode extends DefaultTreeNode {
        private static final long serialVersionUID = -1402486364788361866L;

        public LocalPathTreeNode(Path p, TreeNode parent) {
            super(p, parent);
            for (Path c : p.getChildren()) {
                new LocalPathTreeNode(c, this);
            }
        }
    }
}
