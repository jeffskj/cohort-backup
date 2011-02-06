package org.cohortbackup.backup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.domain.Current;
import org.cohortbackup.util.ChecksumUtils;

public class LocalRepository {
    private static final int BUFFER_SIZE = 4096;
    
    @Inject @Current
    protected Configuration config;
    
    public LocalRepository() {
    }

    public LocalRepository(Configuration config) {
        this.config = config;
    }
    
    public File getFile(UUID id) {
        File f = getRepositoryFile(id);
        return f.exists() ? f : null;
    }
    
    public InputStream get(UUID id) throws IOException {
        return new BufferedInputStream(new GZIPInputStream(new FileInputStream((getRepositoryFile(id)))));
    }
    
    /**
     * @param id
     * @return stream of data in repository in its compressed form
     * @throws IOException
     */
    public InputStream getRaw(UUID id) throws IOException {
        return new BufferedInputStream(new FileInputStream((getRepositoryFile(id))));
    }
    
    /**
     * directly adds data to repository without compressing it, assumes already compressed
     * @param id
     * @param input
     * @throws IOException
     */
    public void putRaw(UUID id, InputStream input) throws IOException {
        IOUtils.copy(input, new FileOutputStream(getRepositoryFile(id)));
    }
    
    public void put(UUID id, InputStream input) throws IOException {
        FileOutputStream fileOutput = new FileOutputStream(getRepositoryFile(id));
        IOUtils.copy(input, new GZIPOutputStream(fileOutput, BUFFER_SIZE));
    }
    
    public void remove(UUID id) {
        getRepositoryFile(id).delete();
    }
    
    public String getRawChecksum(UUID id) {
        return ChecksumUtils.getMD5Digest(getRepositoryFile(id));
    }
    
    private File getRepositoryFile(UUID id) {
        return new File(config.getLocalRepositoryDir(), id.toString());
    }
}
