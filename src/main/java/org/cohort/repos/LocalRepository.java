package org.cohort.repos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cohortbackup.util.ChecksumUtils;

public class LocalRepository {
    private static final int BUFFER_SIZE = 4096;
    private final File root;
    private Index index;
    private File metadata;

    public LocalRepository(File root) {
        this.root = root;
        initialize(root);
    }

    private void initialize(File root) {
        metadata = new File(root, "metadata");
        try {
            index = loadIndex(metadata);
        } catch (IOException e) {
            throw new RuntimeException("error initializing index", e);
        }
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
     * 
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
        return new File(root, id.toString());
    }

    public Index getIndex() {
        return index;
    }

    private static Index loadIndex(File metadata) throws IOException {
        File indexFile = new File(metadata, ".index");
        if (!indexFile.exists()) {
            FileUtils.touch(indexFile);
            JAXB.marshal(new Index(), indexFile);
        }
        
        return JAXB.unmarshal(indexFile, Index.class);
    }

    public void saveIndex() {
        File indexFile = new File(metadata, ".index");
        JAXB.marshal(index, indexFile);
    }
}
