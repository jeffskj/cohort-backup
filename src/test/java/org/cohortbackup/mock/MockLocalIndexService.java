package org.cohortbackup.mock;

import java.io.File;
import java.util.Arrays;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cohortbackup.domain.LocalIndex;
import org.cohortbackup.domain.LocalPath;

import com.google.common.io.Files;

@ApplicationScoped
public class MockLocalIndexService {
    File createTempDir = Files.createTempDir();
    private LocalIndex localIndex;
    
    public File getDir() {
        return createTempDir;
    }
    
    @Produces 
    public LocalIndex getLocalIndex() {
        if (localIndex == null) {
            localIndex = new LocalIndex(Arrays.asList(new LocalPath(createTempDir)));
        }
        return localIndex;
    }
}
