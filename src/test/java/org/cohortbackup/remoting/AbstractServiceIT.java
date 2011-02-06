package org.cohortbackup.remoting;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.cohortbackup.backup.BackupService;
import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.LocalIndex;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.encryption.EncryptionService;
import org.cohortbackup.testing.Mock;
import org.cohortbackup.util.ChecksumUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class AbstractServiceIT 
{
    public static Class[] BASE_CLASSES = new Class[] { Swarm.class, Node.class, BackupItem.class,  
        Mock.class, Current.class, AbstractServiceIT.class, ChecksumUtils.class, Configuration.class, LocalIndex.class};

    public static Class[] BASE_SERVICE_INTERFACES = new Class[] { BackupService.class, EncryptionService.class };
    
    public static final String BASE_URL = "http://localhost:8080/test";

    private static final String BEANS_XML = "<beans><alternatives>" +
             "<stereotype>org.cohortbackup.testing.Mock</stereotype>" +
         "</alternatives></beans>";
    
    public static WebArchive createBaseArchive(boolean wsTest) {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
            .addLibraries(createLibraries())
            .addClasses(BASE_CLASSES).addClasses(BASE_SERVICE_INTERFACES)
            .addManifestResource(new StringAsset(BEANS_XML), "beans.xml");
        if (wsTest) {
            archive.addClasses(UUIDStringConverter.class);
        }
        return archive;
    }

    private static Archive<?> createLibraries() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, StringUtils.class.getPackage())
            .addPackages(true, IOUtils.class.getPackage())
        ;
    }
}
