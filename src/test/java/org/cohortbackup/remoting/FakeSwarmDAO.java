package org.cohortbackup.remoting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;
import org.cohortbackup.persistence.SwarmService;

import com.google.common.collect.Sets;

public class FakeSwarmDAO implements SwarmService {
    private Map<UUID, Swarm> fakeDB = new HashMap<UUID, Swarm>();
    
    @Override
    public Swarm getSwarm(UUID id) {
        if (!fakeDB.containsKey(id)) {
            Swarm swarm = new Swarm();
            swarm.setId(id);
            Node node = new Node();
            node.setName("foo");
            node.setUuid(UUID.randomUUID());
            swarm.setNodes(Sets.newHashSet(node));
            fakeDB.put(id, swarm);
        }
        return fakeDB.get(id);
    }

    @Override
    public Swarm getCurrentSwarm() {
        return null;
    }

    @Override
    public Node getCurrentNode() {
        return null;
    }
}