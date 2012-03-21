package org.cohortbackup.backup;

import java.util.List;

import org.cohort.repos.LocalRepository;
import org.cohort.repos.Path;

public class BackupSendServiceImpl implements BackupSendService {

    @Override
    public void sendBackups(LocalRepository repos) {
        List<Path> unsentPaths = repos.getIndex().getUnsentPaths();
        for (Path p : unsentPaths) {
            
        }
    }

}
