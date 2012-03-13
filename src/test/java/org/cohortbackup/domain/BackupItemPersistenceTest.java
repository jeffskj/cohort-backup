package org.cohortbackup.domain;

import org.junit.Test;

import com.jeffskj.testing.persistence.PersistenceTester;

public class BackupItemPersistenceTest {
    @Test
    public void testCrud() {
        PersistenceTester<BackupItem> tester = new PersistenceTester<BackupItem>(BackupItem.class, "cohort");
        tester.createReadUpdateDelete();
    }
}
