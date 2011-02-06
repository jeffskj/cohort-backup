package org.cohortbackup.backup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.cohortbackup.domain.LocalPath;
import org.cohortbackup.domain.MockConfiguration;
import org.cohortbackup.encryption.MockEncryptionService;
import org.cohortbackup.persistence.MockLocalIndexService;
import org.cohortbackup.persistence.MockSwarmService;
import org.cohortbackup.persistence.SwarmService;
import org.cohortbackup.remoting.AbstractServiceIT;
import org.cohortbackup.remoting.BackupItemWebServiceClient;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.io.Files;
import com.google.common.io.Resources;

@RunWith(Arquillian.class)
public class BackupServiceIT extends AbstractServiceIT
{
    private static final String FAKE_DOCUMENT_TO_BACKUP = "LoremIpsum.docx";

    @Deployment
    public static WebArchive createTestArchive() {
        return createBaseArchive(false)
            .addClasses(MockConfiguration.class, MockLocalIndexService.class) 
            .addClasses(LocalPath.class, SwarmService.class,  MockSwarmService.class)
            .addClasses(BackupItemWebServiceClient.class, MockBackupItemWebServiceClient.class)
            .addClasses(MockEncryptionService.class)
            .addClasses(BackupServiceImpl.class, LocalRepository.class)
            .addResource(FAKE_DOCUMENT_TO_BACKUP, "WEB-INF/classes/" + FAKE_DOCUMENT_TO_BACKUP);
    }
    
    @Inject
    MockLocalIndexService indexService;

    @Inject
    BackupService backupService;
    
    @Test
    public void getBackupItemContents() throws FileNotFoundException, IOException {
        File dir = indexService.getDir();
        try {
            URL resource = getClass().getClassLoader().getResource(FAKE_DOCUMENT_TO_BACKUP);
            Resources.copy(resource, new FileOutputStream(new File(dir, FAKE_DOCUMENT_TO_BACKUP)));
            backupService.backup();
        } finally {
            try {
                Files.deleteRecursively(indexService.getDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @AfterClass
    public static void cleanupTemp() throws IOException {
        File tempDir = FileUtils.getTempDirectory();
        File[] files = tempDir.listFiles((FilenameFilter)new WildcardFileFilter("testing-repos-dir*"));
        for (File file : files) {
            System.out.println("deleting " + file);
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                System.out.println("Failed to delete " + file);
                System.out.println(e.getMessage());
            }
        }
    }
}
