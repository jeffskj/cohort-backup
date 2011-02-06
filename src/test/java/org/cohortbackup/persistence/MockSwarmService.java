package org.cohortbackup.persistence;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;

import com.google.common.collect.Sets;

@ApplicationScoped
public class MockSwarmService implements SwarmService {
    private Swarm swarm;
    private Node currentNode;
    
    public MockSwarmService() {
        swarm = new Swarm();
        Node node1 = new Node();
        node1.setUuid(UUID.randomUUID());
        node1.setFreeSpace(Long.MAX_VALUE/2);
        node1.setIpAddress("127.0.0.1");
        
        Node node2 = new Node();
        node2.setUuid(UUID.randomUUID());
        node2.setFreeSpace(0);
        node2.setIpAddress("127.0.0.1");
        
        currentNode = new Node();
        currentNode.setUuid(UUID.randomUUID());
        currentNode.setFreeSpace(Long.MAX_VALUE/10);
        currentNode.setIpAddress("127.0.0.1");
        
        swarm.setId(UUID.randomUUID());
        swarm.setNodes(Sets.newHashSet(node1, node2, currentNode));
    }
    
    @Override
    public Swarm getSwarm(UUID id) {
        return swarm;
    }
    
    @Override
    @Produces @Current
    public Swarm getCurrentSwarm() {
        return swarm;
    }
    
    @Override 
    @Produces @Current
    public Node getCurrentNode() {
        return currentNode;
    }
}
