package org.cohortbackup.persistence;

import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.cohortbackup.domain.BackupItem;

public class BackupItemServiceImpl implements BackupItemService {

    @Inject EntityManager em;

    @Override
    public BackupItem getBackupItem(UUID id) {
        return em.find(BackupItem.class, id);
    }

    @Override
    public void save(BackupItem item) {
        em.persist(item);
    }
}
