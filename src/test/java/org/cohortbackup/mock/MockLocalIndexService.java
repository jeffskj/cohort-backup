package org.cohortbackup.mock;

import java.io.File;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cohort.repos.Index;
import org.cohort.repos.Path;

import com.google.common.io.Files;

@ApplicationScoped
public class MockLocalIndexService {
    File createTempDir = Files.createTempDir();
    private Index localIndex;

    public File getDir() {
        return createTempDir;
    }

    @Produces
    public Index getLocalIndex() {
        if (localIndex == null) {
            localIndex = new Index(Arrays.asList(new Path(createTempDir)));
        }
        return localIndex;
    }
}
