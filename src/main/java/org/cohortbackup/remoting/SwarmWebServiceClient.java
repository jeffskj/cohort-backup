package org.cohortbackup.remoting;

import java.util.UUID;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;

public interface SwarmWebServiceClient {

    public abstract void addSwarmMember(UUID id, Node node);

    public abstract Swarm getSwarm(UUID id);

}