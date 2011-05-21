package org.cohortbackup.remoting;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.cohortbackup.backup.BackupService;
import org.cohortbackup.domain.Node;
import org.cohortbackup.encryption.EncryptionService;
import org.cohortbackup.persistence.PersistenceFactory;
import org.cohortbackup.util.ChecksumUtils;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.core.MethodInjectorImpl;
import org.jboss.resteasy.core.PathParamInjector;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class AbstractServiceIT 
{
//    public static Class[] BASE_CLASSES = new Class[] { Swarm.class, Node.class, BackupItem.class,  
//        Current.class, AbstractServiceIT.class, ChecksumUtils.class,
//        Configuration.class, LocalIndex.class, UUIDStringConverter.class};
//
//    public static Class[] BASE_SERVICE_INTERFACES = new Class[] { BackupService.class, EncryptionService.class };

    private static Package[] BASE_PACKAGES = new Package[] {
        BackupService.class.getPackage(), 
        Node.class.getPackage(),
        EncryptionService.class.getPackage(),
        PersistenceFactory.class.getPackage(),
        BackupItemWebService.class.getPackage(),
        ChecksumUtils.class.getPackage()
    };
    
    public static final String BASE_URL = "http://localhost:8080/test";

    private static final String BEANS_XML = "<beans><alternatives>" +
             "<stereotype>org.cohortbackup.testing.Mock</stereotype>" +
         "</alternatives>" +
         "</beans>";
    
    public static WebArchive createBaseArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
//            .addAsLibraries(createLibraries())
//            .addClasses(BASE_CLASSES).addClasses(BASE_SERVICE_INTERFACES)
            .addPackages(true, BASE_PACKAGES)
            .addAsWebInfResource("META-INF/test-beans.xml", "classes/META-INF/beans.xml")
            .addAsWebInfResource("META-INF/test-persistence.xml", "classes/META-INF/persistence.xml")
            .addAsWebInfResource("logback-test.xml", "classes/logback-test.xml")
            .addAsWebInfResource("web.xml")
            .addAsWebInfResource("jetty-env.xml");
        return archive;
    }

    private static Archive<?> createLibraries() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, StringUtils.class.getPackage())
            .addPackages(true, IOUtils.class.getPackage())
            .addPackages(true, POJOResourceFactory.class.getPackage())
            .addPackages(true, ResourceFactory.class.getPackage())
            .addPackages(true, MethodInjectorImpl.class.getPackage())
            .addPackages(true, ClientExecutor.class.getPackage())
            .addPackages(true, PathParamInjector.class.getPackage())
        ;
    }
}
