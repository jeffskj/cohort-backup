package org.cohortbackup.remoting;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.httpclient.HttpException;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.persistence.SwarmService;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.Run;
import org.jboss.arquillian.api.RunModeType;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


@Run(RunModeType.AS_CLIENT)
@RunWith(Arquillian.class)
public class SwarmWebServiceImplIT extends AbstractServiceIT
{
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "cohort-test.war")
            .addClasses(SwarmService.class, SwarmWebServiceImpl.class, SwarmService.class, FakeSwarmDAO.class)
            .addClasses(BASE_CLASSES).addManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    
    @Test
    public void addMemberToSwarm() throws HttpException, IOException {
        SwarmWebServiceClientImpl client = new SwarmWebServiceClientImpl("http://localhost:8080/cohort-test/");
        Swarm s = client.getSwarm(UUID.randomUUID());
        
        assertEquals(1, s.getNodes().size());
        System.out.println(s.getId());
        
        Node node = new Node();
        node.setName("other");
        node.setTotalSpace(123L);
        s.getNodes().add(node);
        client.addSwarmMember(s.getId(), node);
        Set<Node> members = client.getSwarm(s.getId()).getNodes();
        assertEquals(2, members.size());
    }
}
