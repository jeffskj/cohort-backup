package org.cohortbackup.remoting;

import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.persistence.SwarmService;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

@Path("/swarm")
@ApplicationScoped
public class SwarmWebServiceImpl implements SwarmWebService
{
    @Inject
    SwarmService swarmService;
    
    @GET
    @Path("/{id}")
    @Produces("application/xml")
    public Swarm getSwarm(@PathParam("id")UUID id) 
    {
        return swarmService.getSwarm(id);
    }
    
    @GET
    @Path("/{id}/members")
    @Produces("application/xml")
    @Wrapped(element="nodes")
    public Set<Node> getSwarmMembers(@PathParam("id") UUID id) 
    {
        return swarmService.getSwarm(id).getNodes();
    }
    
    @PUT
    @Path("/{id}/members")
    @Consumes("application/xml")
    public void addSwarmMember(@PathParam("id")UUID id, Node node) 
    {
        swarmService.getSwarm(id).getNodes().add(node);
    }
}
