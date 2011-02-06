package org.cohortbackup.persistence;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.domain.Node;
import org.cohortbackup.domain.Swarm;

@ApplicationScoped
public class SwarmServiceImpl implements SwarmService {
    @Inject EntityManager em;
    @Inject @Current Configuration config;
    
    @Override
    public Swarm getSwarm(UUID id) {
        Swarm s = em.find(Swarm.class, id);
        if (s == null) {
            s = new Swarm();
            s.setId(id);
            em.persist(s);
        }
        return s;
    }
    
    @Override
    @Produces @Current
    public Swarm getCurrentSwarm() {
        return getSwarm(config.getSwarmId());
    }
    
    @Override 
    @Produces @Current
    public Node getCurrentNode() {
        return em.find(Node.class, config.getNodeId());
    }
}
