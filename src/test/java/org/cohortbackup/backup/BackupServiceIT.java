package org.cohortbackup.backup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.cohortbackup.remoting.AbstractServiceIT;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BackupServiceIT extends AbstractServiceIT {
    private static final String FAKE_DOCUMENT_TO_BACKUP = "LoremIpsum.docx";

    @Deployment
    @TargetsContainer("container-1")
    public static WebArchive createTestArchive() {
        return createBaseArchive()
        // .addClasses(MockConfiguration.class, MockLocalIndexService.class)
        // .addClasses(LocalPath.class, SwarmService.class, MockSwarmService.class)
        // .addClasses(BackupItemWebServiceClient.class, MockBackupItemWebServiceClient.class)
        // .addClasses(MockEncryptionService.class)
        // .addClasses(BackupServiceImpl.class, LocalRepository.class)
                .addAsResource(FAKE_DOCUMENT_TO_BACKUP, FAKE_DOCUMENT_TO_BACKUP);
    }

    // @Inject
    // MockLocalIndexService indexService;

    @Inject
    BackupService backupService;

    @Test
    public void getBackupItemContents() throws FileNotFoundException, IOException {
        // File dir = indexService.getDir();
        // URL resource = getClass().getClassLoader().getResource(FAKE_DOCUMENT_TO_BACKUP);
        // Resources.copy(resource, new FileOutputStream(new File(dir, FAKE_DOCUMENT_TO_BACKUP)));
        // backupService.backup();
    }

    @AfterClass
    public static void cleanupTemp() throws IOException {
        File tempDir = FileUtils.getTempDirectory();
        File[] files = tempDir.listFiles((FilenameFilter) new WildcardFileFilter("testing-repos-dir*"));
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
