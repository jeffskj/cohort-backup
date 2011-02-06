package org.cohortbackup.domain;

import org.junit.Test;

import com.jeffskj.testing.persistence.PersistenceTester;

public class NodePersistenceTest 
{
    @Test
    public void testCrud()
    {
        PersistenceTester<Node> tester = new PersistenceTester<Node>(Node.class, "cohort");
        tester.createReadUpdateDelete();
    }
}
