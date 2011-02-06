package org.cohortbackup.domain;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

public class LocalIndexTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void addsNewFileIntoIndex() throws IOException
    {
        folder.newFile("foo.txt");
        
        LocalPath path = new LocalPath(folder.getRoot());
        path.refreshChildren();
        
        folder.newFile("bar.txt");
        
        LocalIndex index = new LocalIndex(Arrays.asList(path));
        
        List<LocalPath> outOfDatePaths = index.getOutOfDatePaths();
        assertEquals(2, outOfDatePaths.size());
        System.out.println(outOfDatePaths);
    }

    @Test
    public void addsNewFileInSubFolderIntoIndex() throws IOException
    {
        File subFolder = folder.newFolder("sub");
        
        LocalPath path = new LocalPath(folder.getRoot());
        path.refreshChildren();
        
        new File(subFolder, "foo.txt").createNewFile();
        
        LocalIndex index = new LocalIndex(Arrays.asList(path));
        
        assertEquals(1, index.getOutOfDatePaths().size());
    }

    @Test
    public void doesntAddFoldersToBackupQueue() throws IOException
    {
        folder.newFolder("sub");
        
        LocalPath path = new LocalPath(folder.getRoot());
        LocalIndex index = new LocalIndex(Arrays.asList(path));

        List<LocalPath> outOfDatePaths = index.getOutOfDatePaths();
        assertEquals(0, outOfDatePaths.size());
        System.out.println(outOfDatePaths);
    }
    
    @Test
    public void findsDeletedFiles() throws IOException {
        File subFolder = folder.newFolder("sub");
        new File(subFolder, "foo.txt").createNewFile();        
        
        LocalPath path = new LocalPath(folder.getRoot());
        path.refreshChildren();
        LocalPath foo = path.getChild("sub").getChild("foo.txt");
        BackupItem backupItem = new BackupItem();
        backupItem.setBackupDate(new Date());
        foo.setBackupItems(Arrays.asList(backupItem));
        
        LocalIndex index = new LocalIndex(Arrays.asList(path));
        
        foo.getFile().delete();
        
        assertEquals(1, index.getDeletedPaths().size());
    }
    
    @Test
    public void tryOnRealData() {
        LocalPath path = new LocalPath(new File("D:\\img"));
        LocalIndex idx = new LocalIndex(Lists.newArrayList(path));
        List<LocalPath> outOfDatePaths = idx.getOutOfDatePaths();
        
        for (LocalPath p : outOfDatePaths) {
            System.out.println(p.getFile().getAbsolutePath());
        }
        System.out.println(outOfDatePaths.size());
    }
}
