package org.cohortbackup.persistence;

import java.util.UUID;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;

public class ConfigurationService {
    @Inject EntityManager em;
    
    @Produces @Current
    public Configuration getCurrentConfiguration() {
        Configuration config = em.find(Configuration.class, Configuration.ID);
        if (config == null) {
            config = new Configuration();
            config.setNodeId(UUID.randomUUID());
            em.persist(config);
        }
        
        return config;
    }
    
    public void save(Configuration config) {
        em.merge(config);
    }
}
