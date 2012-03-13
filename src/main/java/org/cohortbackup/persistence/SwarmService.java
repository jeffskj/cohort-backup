package org.cohortbackup.persistence;

import java.util.UUID;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;

public interface SwarmService {
    Swarm getSwarm(UUID id);

    Swarm getCurrentSwarm();

    Node getCurrentNode();

    void addNode(UUID id, Node node);
}
