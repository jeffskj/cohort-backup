package org.cohortbackup.domain;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RemotePathTest
{
    @Test
    public void addBackupItem()
    {
        RemotePath foo = new RemotePath("foo");
        foo.addBackupItem("bar/baz.txt", new BackupItem());
        
        assertEquals(1, foo.getChildren().size());
        RemotePath bar = foo.getChild("bar");
        assertNotNull(bar);
        
        RemotePath baz = bar.getChild("baz.txt");
        assertNotNull(baz);
        assertEquals(1, baz.getBackupItems().size());
        
        foo.addBackupItem("path/to/this/item.txt", new BackupItem());
        foo.addBackupItem("path/to/this/other/item.txt", new BackupItem());
        
        RemotePath pathToThis = foo.getChild("path").getChild("to").getChild("this");
        assertEquals(2, pathToThis.getChildren().size());
        
        assertEquals(1, pathToThis.getChild("item.txt").getBackupItems().size());
        assertEquals(1, pathToThis.getChild("other").getChild("item.txt").getBackupItems().size());
    }
}
