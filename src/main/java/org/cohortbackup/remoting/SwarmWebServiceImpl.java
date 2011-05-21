package org.cohortbackup.remoting;

import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.persistence.SwarmService;

@ApplicationScoped
public class SwarmWebServiceImpl implements SwarmWebService
{
    @Inject
    SwarmService swarmService;
    
    public Swarm getSwarm(@PathParam("id")UUID id) 
    {
        return swarmService.getSwarm(id);
    }
    
    public Set<Node> getSwarmMembers(@PathParam("id") UUID id) 
    {
        return swarmService.getSwarm(id).getNodes();
    }
    
    public void addSwarmMember(@PathParam("id")UUID id, Node node) 
    {
        swarmService.addNode(id, node);
    }

    public void setMembers(UUID id, Set<Node> nodes) {
        swarmService.getSwarm(id).setNodes(nodes);
    }
}
