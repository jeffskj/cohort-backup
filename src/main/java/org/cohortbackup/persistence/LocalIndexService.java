package org.cohortbackup.persistence;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.cohort.repos.Index;
import org.cohort.repos.Path;

@ApplicationScoped
public class LocalIndexService {

    @Inject
    EntityManager em;

    private Index localIndex;

    @Produces
    @ApplicationScoped
    @Named
    public Index getLocalIndex() {
        if (localIndex == null) {
            List<Path> paths = em.createQuery("from LocalPath where parent is null", Path.class).getResultList();
            localIndex = new Index(paths);
        }
        return localIndex;
    }

    public void addRootPath(Path path) {
        getLocalIndex().getRoots().add(path);
        em.persist(path);
    }
}
