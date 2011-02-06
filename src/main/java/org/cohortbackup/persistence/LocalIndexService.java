package org.cohortbackup.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.cohortbackup.domain.LocalIndex;
import org.cohortbackup.domain.LocalPath;

@ApplicationScoped 
public class LocalIndexService {

    @Inject EntityManager em;

    private LocalIndex localIndex;
    
    @Produces @ApplicationScoped @Named
    public LocalIndex getLocalIndex() {
        if (localIndex == null) {
            List<LocalPath> paths = em.createQuery("from LocalPath where parent is null", LocalPath.class).getResultList();
            localIndex = new LocalIndex(paths);
        }
        return localIndex;
    }
    
    public void addRootPath(LocalPath path) {
        getLocalIndex().getBackupRoots().add(path);
        em.persist(path);
    }
}
