package org.cohort.repos;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.cohortbackup.domain.BackupClient;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.BackupLocation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LocalRepositoryTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void canSaveAndLoadIndex() throws IOException {
        LocalRepository repos = new LocalRepository(tmp.getRoot());
        assertEquals(0, repos.getIndex().getRoots().size());
        
        File root1 = tmp.newFolder("root1");
        tmp.newFile("root1/file.txt");
        tmp.newFile("root1/file2.txt");
        File root2 = tmp.newFolder("root2");
        tmp.newFile("root2/file.txt");
        
        repos.getIndex().addRoot(new Path(root1));
        repos.getIndex().getRoots().get(0).getChildren().get(0).getBackupItems().add(createBackupItem());
        repos.getIndex().getRoots().get(0).getChildren().get(0).getBackupItems().add(createBackupItem());
        repos.getIndex().getRoots().get(0).getChildren().get(0).getBackupItems().add(createBackupItem());
        
        repos.getIndex().addRoot(new Path(root2));
        repos.getIndex().getRoots().get(1).getChildren().get(0).getBackupItems().add(createBackupItem());
        
        assertEquals(2, repos.getIndex().getRoots().size());
        repos.saveIndex();
        
        File indexFile = new File(tmp.getRoot(), "metadata/.index");
//        System.out.println(FileUtils.readFileToString(indexFile));
        System.out.println("length: " + indexFile.length());
        String indexString = IOUtils.toString(new GZIPInputStream(new FileInputStream(indexFile)));
        System.out.println("raw length: " + indexString.getBytes().length);
        System.out.println(indexString);
        
        assertTrue(indexFile.length() > 0);
        repos = new LocalRepository(tmp.getRoot());
        
        assertEquals(2, repos.getIndex().getRoots().size());        
    }
    
    @Test
    public void canSaveAndLoadBackupLog() throws IOException {
        LocalRepository repos = new LocalRepository(tmp.getRoot());
        BackupLog log = repos.getBackupLog();
        assertEquals(0, log.getLogEntries().size());
        int numEntries = 1000;
        
        for (int i = 0; i < numEntries; i++) {
            log.addEntry(createBackupItem(), new MockBackupLocation());
        }
        assertEquals(numEntries, log.getLogEntries().size());
        repos.saveBackupLog();
        
        File indexFile = new File(tmp.getRoot(), "metadata/.backuplog");
//        System.out.println(FileUtils.readFileToString(indexFile));
        System.out.println("length: " + indexFile.length());
        String indexString = IOUtils.toString(new GZIPInputStream(new FileInputStream(indexFile)));
        System.out.println("raw length: " + indexString.getBytes().length);
//        System.out.println(indexString);
        
        assertTrue(indexFile.length() > 0);
        repos = new LocalRepository(tmp.getRoot());
        
        assertEquals(numEntries, repos.getBackupLog().getLogEntries().size());        
    }

    private BackupItem createBackupItem() {
        BackupItem backupItem = new BackupItem();
        backupItem.setId(UUID.randomUUID());
        backupItem.setSize(RandomUtils.nextLong());
        backupItem.setBackupDate(new Date());
        backupItem.setVersion(RandomUtils.nextInt(100));
        return backupItem;
    }
    
    private static class MockBackupLocation implements BackupLocation {
        @Override
        public UUID getId() {
            return UUID.randomUUID();
        }

        @Override
        public BackupClient getBackupClient() {
            return null;
        }
    }
}
