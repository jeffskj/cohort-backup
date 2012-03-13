package org.cohort.repos;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.cohortbackup.domain.BackupItem;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

public class IndexTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void addsNewFileIntoIndex() throws IOException {
        folder.newFile("foo.txt");

        Path path = new Path(folder.getRoot());
        path.refreshChildren();

        folder.newFile("bar.txt");

        Index index = new Index(Arrays.asList(path));

        List<Path> outOfDatePaths = index.getOutOfDatePaths();
        assertEquals(2, outOfDatePaths.size());
        System.out.println(outOfDatePaths);
    }

    @Test
    public void addsNewFileInSubFolderIntoIndex() throws IOException {
        File subFolder = folder.newFolder("sub");

        Path path = new Path(folder.getRoot());
        path.refreshChildren();

        new File(subFolder, "foo.txt").createNewFile();

        Index index = new Index(Arrays.asList(path));

        assertEquals(1, index.getOutOfDatePaths().size());
    }

    @Test
    public void doesntAddFoldersToBackupQueue() throws IOException {
        folder.newFolder("sub");

        Path path = new Path(folder.getRoot());
        Index index = new Index(Arrays.asList(path));

        List<Path> outOfDatePaths = index.getOutOfDatePaths();
        assertEquals(0, outOfDatePaths.size());
        System.out.println(outOfDatePaths);
    }

    @Test
    public void findsDeletedFiles() throws IOException {
        File subFolder = folder.newFolder("sub");
        new File(subFolder, "foo.txt").createNewFile();

        Path path = new Path(folder.getRoot());
        path.refreshChildren();
        Path foo = path.getChild("sub").getChild("foo.txt");
        BackupItem backupItem = new BackupItem();
        backupItem.setBackupDate(new Date());
        foo.getBackupItems().add(backupItem);

        Index index = new Index(Arrays.asList(path));

        foo.getFile().delete();

        assertEquals(1, index.getDeletedPaths().size());
    }

    @Test
    @Ignore
    public void tryOnRealData() {
        Path path = new Path(new File("D:\\img"));
        Index idx = new Index(Lists.newArrayList(path));
        List<Path> outOfDatePaths = idx.getOutOfDatePaths();

        for (Path p : outOfDatePaths) {
            System.out.println(p.getFile().getAbsolutePath());
        }
        System.out.println(outOfDatePaths.size());
    }
}
