package org.cohortbackup.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

public class LocalPathTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void addsNewFile() throws IOException
    {
        folder.newFile("bar.txt");

        LocalPath path = new LocalPath(folder.getRoot());
        
        assertEquals(0, path.getChildren().size());
        path.refreshChildren();
        assertEquals(1, path.getChildren().size());        
    }
    
    @Test
    public void checksIfDeleted() throws IOException
    {
        LocalPath bar = new LocalPath(folder.newFile("bar.txt"));
        assertFalse(bar.isDeleted());
        bar.getFile().delete();
        assertTrue(bar.isDeleted());
    }
    
    @Test
    public void setsParentInfoCorrectly() throws IOException
    {
        folder.newFile("bar.txt");

        LocalPath path = new LocalPath(folder.getRoot());
        
        path.refreshChildren();
        
        LocalPath bar = path.getChildren().iterator().next();
        assertSame(path, bar.getParent());
    }
    
    @Test
    public void checksIfBackedUp() throws IOException
    {
        LocalPath bar = new LocalPath(folder.newFile("bar.txt"));
        assertFalse(bar.isBackedUp());
        bar.setBackupItems(Lists.newArrayList(new BackupItem()));
        bar.getBackupItems().get(0).setBackupDate(new Date());
        assertTrue(bar.isBackedUp());
    }
    
    @Test
    public void checksIfOutOfDate() throws IOException, InterruptedException
    {
        LocalPath bar = new LocalPath(folder.newFile("bar.txt"));
        assertTrue(bar.isOutOfDate());
        BackupItem backupItem = new BackupItem();
        backupItem.setBackupDate(new Date());
        bar.setBackupItems(Lists.newArrayList(backupItem));
        assertFalse(bar.isOutOfDate());

        bar.getFile().setLastModified(System.currentTimeMillis()+1000);
        assertTrue(bar.isOutOfDate());
    }
    
    @Test
    public void correctlyInheritsPriority() throws IOException 
    {
        LocalPath path = new LocalPath(folder.getRoot());
        path.setPriority(10);
        File bazFolder = new File(folder.getRoot(), "foo/bar/baz");
        bazFolder.mkdirs();
        new File(bazFolder, "test.txt").createNewFile();
        
        path.refreshChildren();
        
        LocalPath baz = path.getChildren().iterator().next()
                .getChildren().iterator().next()
                .getChildren().iterator().next();
        
        assertEquals(10, baz.getPriority());
    }
    
    @Test
    public void getARandomUUID() {
        System.out.println(UUID.randomUUID());
    }
}
