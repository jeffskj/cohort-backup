package org.cohortbackup.persistence;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.persistence.EntityManager;

import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.remoting.SwarmWebService;
import org.easymock.EasyMockSupport;
import org.junit.Test;

public class SwarmServiceImplTest extends EasyMockSupport {

    @Test
    public void testInit() {
        SwarmServiceImpl service = new SwarmServiceImpl();
        service.config = new Configuration();
        service.config.setNodeId(UUID.randomUUID());
        service.config.setSwarmId(UUID.randomUUID());
        
        service.em = createMock(EntityManager.class);
        expect(service.em.find(Node.class, service.config.getNodeId())).andReturn(null);
        service.em.persist(isA(Node.class));
        
        expect(service.em.find(Swarm.class, service.config.getSwarmId())).andReturn(null);
        expect(service.em.find(Node.class, service.config.getNodeId())).andReturn(new Node());
        service.em.persist(isA(Swarm.class));
        
        replayAll();
        service.init();
        verifyAll();
    }
    
    @Test
    public void addNodeToSwarm() {
        final Swarm s = new Swarm();
        s.setId(UUID.randomUUID());
        
        final Node coordinator = new Node();
        coordinator.setSwarm(s);
        coordinator.setId(UUID.randomUUID());
        s.setCoordinator(coordinator);
        
        final Node current = new Node();
        current.setSwarm(new Swarm());
        current.getSwarm().setId(UUID.randomUUID());
        
        final Node existing = new Node();
        existing.setId(UUID.randomUUID());
        existing.setSwarm(s);
        s.getNodes().add(existing);
        s.getNodes().add(coordinator);
        
        final SwarmWebService remoteService = createMock(SwarmWebService.class);
        remoteService.addSwarmMember(isA(UUID.class), isA(Node.class));
        expectLastCall().times(2);
        
        SwarmServiceImpl service = new SwarmServiceImpl() {
            @Override
            SwarmWebService getClient(Node n) {
                return remoteService;
            }  
        };
        
        service.config = new Configuration();
        service.config.setNodeId(UUID.randomUUID());
        service.config.setSwarmId(current.getSwarm().getId());
        
        current.setId(service.config.getNodeId());
        current.getSwarm().setId(service.config.getSwarmId());
        
        service.em = createMock(EntityManager.class);
        expect(service.em.find(Swarm.class, s.getId())).andReturn(s).anyTimes();
        expect(service.em.find(Node.class, current.getId())).andReturn(current).anyTimes();
        expect(service.em.find(Node.class, coordinator.getId())).andReturn(coordinator).anyTimes();
        
        replayAll();
        
        service.addNode(s.getId(), current);
        
        service.config.setNodeId(coordinator.getId());
        service.addNode(s.getId(), coordinator);
        
        verifyAll();
    }
    
    @Test
    public void joinSwarm() {
        final Swarm s = new Swarm();
        s.setId(UUID.randomUUID());
        
        final Node coordinator = new Node();
        coordinator.setSwarm(s);
        coordinator.setId(UUID.randomUUID());
        s.setCoordinator(coordinator);
        
        final Node current = new Node();
        current.setSwarm(new Swarm());
        current.getSwarm().setId(UUID.randomUUID());
        
        final Node existing = new Node();
        existing.setId(UUID.randomUUID());
        existing.setSwarm(s);
        s.getNodes().add(existing);
        s.getNodes().add(coordinator);
        
        final SwarmWebService existingService = createMock(SwarmWebService.class);
        expect(existingService.getSwarm(s.getId())).andReturn(s);
        
        final SwarmWebService coordinatorService = createMock(SwarmWebService.class);
        coordinatorService.addSwarmMember(s.getId(), current);
        
        SwarmServiceImpl service = new SwarmServiceImpl() {
            @Override
            SwarmWebService getClient(Node n) {
                if (n == existing) {
                    return existingService;
                }
                
                if (n == coordinator) {
                    return coordinatorService;
                }
                
                return null;
            }
        };
        
        service.config = new Configuration();
        service.config.setNodeId(UUID.randomUUID());
        service.config.setSwarmId(current.getSwarm().getId());
        
        current.setId(service.config.getNodeId());
        current.getSwarm().setId(service.config.getSwarmId());
        
        service.em = createMock(EntityManager.class);
        expect(service.em.find(Swarm.class, service.config.getSwarmId())).andReturn(current.getSwarm()).anyTimes();
        expect(service.em.find(Node.class, current.getId())).andReturn(current).anyTimes();
        service.em.persist(s);
        
        replayAll();
        
        service.joinSwarm(existing);
        
        assertEquals(3, s.getNodes().size());
        assertEquals(s.getId(), service.config.getSwarmId());
        
        verifyAll();
    }
}
