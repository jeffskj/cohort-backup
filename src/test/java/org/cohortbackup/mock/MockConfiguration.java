package org.cohortbackup.mock;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.FileUtils;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.testing.Mock;

import com.google.common.io.Files;

@Mock
@Current
@ApplicationScoped
public class MockConfiguration extends Configuration {
    private File reposDir;

    public MockConfiguration() {
        reposDir = Files.createTempDir();
    }

    @Override
    public File getLocalRepositoryDir() {
        return reposDir;
    }
    
    @Override
    public String getEncryptionKey() {
        return "test";
    }
    
    @PreDestroy
    public void cleanup() {
        try {
            FileUtils.deleteDirectory(reposDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
