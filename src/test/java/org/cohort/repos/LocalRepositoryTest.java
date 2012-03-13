package org.cohort.repos;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.cohortbackup.domain.BackupItem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LocalRepositoryTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void canInitializeALocalRepo() throws IOException {
        LocalRepository repos = new LocalRepository(tmp.getRoot());
        assertEquals(0, repos.getIndex().getRoots().size());
        
        File root1 = tmp.newFolder("root1");
        tmp.newFile("root1/file.txt");
        File root2 = tmp.newFolder("root2");
        
        repos.getIndex().addRoot(new Path(root1));
        repos.getIndex().addRoot(new Path(root2));
        BackupItem backupItem = new BackupItem();
        backupItem.setId(UUID.randomUUID());
        backupItem.setSize(123456L);
        backupItem.setBackupDate(new Date());
        backupItem.setVersion(1);
        
        repos.getIndex().getRoots().get(0).getChildren().get(0).getBackupItems().add(backupItem);
        
        assertEquals(2, repos.getIndex().getRoots().size());
        repos.saveIndex();
        
        repos = new LocalRepository(tmp.getRoot());
        System.out.println(FileUtils.readFileToString(new File(tmp.getRoot(), "metadata/.index")));        
        
        assertEquals(2, repos.getIndex().getRoots().size());        
    }
}
