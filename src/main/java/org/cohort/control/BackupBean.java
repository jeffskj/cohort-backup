package org.cohort.control;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.cohortbackup.backup.BackupService;
import org.jboss.seam.transaction.Transactional;

@Model
public class BackupBean {
    @Inject
    BackupService backupService;
    
    @Transactional
    public void backup() {
        backupService.backup();
    }
}
