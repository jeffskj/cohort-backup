package org.cohortbackup.remoting;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.test.EmbeddedContainer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractClientTest {
    private static Dispatcher dispatcher;

    @BeforeClass
    public static void before() throws Exception {
        dispatcher = EmbeddedContainer.start().getDispatcher();
        dispatcher.getProviderFactory().addStringConverter(UUIDStringConverter.class);
    }

    @AfterClass
    public static void after() throws Exception {
        EmbeddedContainer.stop();
    }

    protected static Dispatcher getEmbeddedDispatcher() {
        return dispatcher;
    }

    protected String getBaseUrl() {
        return TestPortProvider.generateBaseUrl();
    }

    protected void addResource(Class<?> resource) {
        getEmbeddedDispatcher().getRegistry().addResourceFactory(new POJOResourceFactory(resource));
    }
}
