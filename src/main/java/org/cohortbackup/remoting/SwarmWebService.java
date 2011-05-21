package org.cohortbackup.remoting;

import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;

@Path("/swarm")
public interface SwarmWebService
{
    @GET
    @Path("/{id}")
    @Produces("application/xml")
    Swarm getSwarm(@PathParam("id") UUID id);

    @GET
    @Path("/{id}/members")
    @Produces("application/xml")
    @Wrapped(element = "nodes")
    Set<Node> getSwarmMembers(@PathParam("id") UUID id);

    @PUT
    @Path("/{id}/members")
    @Consumes("application/xml")
    void addSwarmMember(@PathParam("id") UUID id, Node node);
    
    @POST
    @Path("/{id}/members")
    @Consumes("application/xml")
    void setMembers(@PathParam("id") UUID id, Set<Node> nodes);
}