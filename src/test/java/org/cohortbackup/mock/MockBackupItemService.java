package org.cohortbackup.mock;

import java.util.Map;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.cohortbackup.domain.BackupItem;
import org.cohortbackup.persistence.BackupItemService;
import org.cohortbackup.testing.Mock;

import com.google.common.collect.Maps;

@Mock
@ApplicationScoped
public class MockBackupItemService implements BackupItemService {
    private Map<UUID, BackupItem> db = Maps.newHashMap();

    @Override
    public BackupItem getBackupItem(UUID id) {
        return db.get(id);
    }

    @Override
    public void save(BackupItem item) {
        db.put(item.getId(), item);
    }
}
