package org.cohortbackup.remoting;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.RandomStringUtils;
import org.cohortbackup.backup.LocalRepository;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Node;
import org.cohortbackup.mock.MockConfiguration;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BackupItemWebServiceIT extends AbstractServiceIT
{
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Deployment
    public static WebArchive createTestArchive() {
        return createBaseArchive()
            .addClasses(MockConfiguration.class);//, MockBackupItemService.class);
    }
    
    @Test
    public void getBackupItemContents() throws IOException, URISyntaxException, InterruptedException
    {
        Configuration config = new Configuration();
        config.setLocalRepositoryDir(tmp.getRoot());
        BackupItem item = new BackupItem();
        item.setId(UUID.randomUUID());
        
        LocalRepository repos = new LocalRepository(config);
        repos.put(item.getId(), IOUtils.toInputStream(RandomStringUtils.randomAscii(1024*1024*5)));
        item.setSize(repos.getFile(item.getId()).length());
        
        Node n = new Node();
        n.setIpAddress("localhost");
        n.setPort(9090);
        
        System.setProperty("cohort.contextPath", "test");
        BackupItemWebServiceClientImpl client = new BackupItemWebServiceClientImpl();
        client.repository = repos;
        
        client.sendBackupItem(n, item);
        client.sendContents(n, item);
        repos.remove(item.getId());
        client.getContents(n, item.getId());
        
        System.out.println("LOCAL: " + tmp.getRoot());
//        System.out.println(IOUtils.toString((repos.get(item.getId()))));
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
