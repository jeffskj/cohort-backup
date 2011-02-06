package org.cohortbackup.remoting;

import java.util.UUID;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.jboss.resteasy.client.ProxyFactory;

public class SwarmWebServiceClientImpl implements SwarmWebServiceClient 
{
    private SwarmWebService proxy;

    public SwarmWebServiceClientImpl(String url)
    {
        proxy = ProxyFactory.create(SwarmWebService.class, url);
    }
    
    @Override
    public void addSwarmMember(UUID id, Node node)
    {
        proxy.addSwarmMember(id, node);
    }

    @Override
    public Swarm getSwarm(UUID id)
    {
        return proxy.getSwarm(id);
    }
}
