package org.cohortbackup.backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.encryption.BasicEncryptionService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BackupServiceImplTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void canMakeLocalBackup() throws IOException {
        File reposFolder = tmp.newFolder("repos");
        File dataFolder = tmp.newFolder("data");
        String testData = StringUtils.repeat(RandomStringUtils.randomAlphabetic(1000), 10);
        FileUtils.writeStringToFile(new File(dataFolder, "test.txt"), testData);
        
        LocalRepository repos = new LocalRepository(reposFolder);
        repos.getConfig().setEncryptionKey("test");
        Path path = new Path(dataFolder);
        repos.getIndex().addRoot(path);
        
        BackupService backupService = new BackupService();
        backupService.encryptionService = new BasicEncryptionService();
        backupService.backup(repos);
        
        assertFalse(path.getChildren().get(0).getBackupItems().isEmpty());
        BackupItem backupItem = path.getChildren().get(0).getBackupItems().get(0);
        assertNotNull(backupItem.getBackupDate());
        InputStream rawReposData = repos.get(backupItem.getId());
        assertNotNull(rawReposData);
        
        String rawReposString = IOUtils.toString(rawReposData);
//        System.out.println(rawReposString);
        System.out.println(rawReposString.length());
        
        String recoveredData = IOUtils.toString(backupService.recover(repos, backupItem));
        assertEquals(testData, recoveredData);
    }
}
