package org.cohortbackup.backup;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.RandomStringUtils;
import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;
import org.cohortbackup.encryption.BasicEncryptionService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class BackupServiceIT {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    private BackupService backupService = new BackupService();
    {
        backupService.encryptionService = new BasicEncryptionService(); 
    }
    
    BackupSendService sendService = new BackupSendService();
    
    @Test
    public void backupToFolder() throws IOException {
        LocalRepository repos = new LocalRepository(tmp.newFolder("repos"));
        File backupFolder = tmp.newFolder("backup");
        
        FolderBackupLocation backupLocation = new FolderBackupLocation();
        backupLocation.setId(UUID.randomUUID());
        backupLocation.setLocation(backupFolder);
        repos.getConfig().addBackupLocation(backupLocation);
        repos.getConfig().setEncryptionKey("encryption key");
        repos.getConfig().setSecretPasword("top secret");
        
        File dataFolder = tmp.newFolder("data");
        makeFileStructure(dataFolder, 3, 2);
        
        assertEquals(14, getFilesInDir(dataFolder).size());
        
        repos.getIndex().addRoot(new Path(dataFolder));
        backupService.backup(repos);
        sendService.sendBackups(repos);
        
        assertEquals(14, getFilesInDir(backupFolder).size());
    }
    
    private void makeFileStructure(File parent, int depth, int count) throws IOException {
        if (depth == 0) { return; }
        parent.mkdir();
        for (int i = 0; i < count; i++) {
            FileUtils.writeStringToFile(new File(parent, "file" + depth + "-" + i), RandomStringUtils.randomAlphabetic(100));
            makeFileStructure(new File(parent, "folder" + depth + "-" + i), depth-1, count);
        }
    }
    
    private Collection<File> getFilesInDir(File dir) {
        Collection<File> files = FileUtils.listFiles(dir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
		for (File f : files) {
            System.out.println(f);
        }
		return files;
    }
}
