package org.cohort.control;

import java.io.File;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.cohortbackup.domain.LocalIndex;
import org.cohortbackup.domain.LocalPath;
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
    LocalIndex localIndex;
    
    private File pathToAdd;
    private TreeNode root;
    
    @Transactional(TransactionPropagation.REQUIRED)
    public void addRootPath() {
        localIndexService.addRootPath(new LocalPath(pathToAdd));
    }

    public List<LocalPath> getBackupRoots() {
        return localIndexService.getLocalIndex().getBackupRoots();
    }
    
    public TreeNode getRoot() {
        if (root == null) {
            root = new DefaultTreeNode("root", null);
            LocalIndex idx = localIndexService.getLocalIndex();
            idx.getOutOfDatePaths();
            
            for (LocalPath p : idx.getBackupRoots()) {
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

        public LocalPathTreeNode(LocalPath p, TreeNode parent) {
            super(p, parent);
            for (LocalPath c : p.getChildren()) {
                new LocalPathTreeNode(c, this);
            }
        }
    }
}
