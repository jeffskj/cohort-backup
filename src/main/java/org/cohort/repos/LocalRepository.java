package org.cohort.repos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXB;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cohortbackup.domain.Configuration;
import org.cohortbackup.util.ChecksumUtils;

/*
 * metadata backup algorithm:
 * configuration has generated encryption key
 * encrypt config file with user supplied password, also stored in config, not encrypted locally
 * encrypt all other metadata files with generated key
 * nothing encrypted locally, just gzipped (why waste the CPU)
 * 
 * 
 */
public class LocalRepository {
    private static final int BUFFER_SIZE = 4096;
    private final File root;
    private Index index;
    private BackupLog backupLog;
    private Configuration config;
    private File indexFile;
    private File metadata;
    private File backupLogFile;
    private File configFile;

    public LocalRepository(File root) {
        this.root = root;
        initialize(root);
    }

    private void initialize(File root) {
        metadata = new File(root, "metadata");
        configFile = new File(metadata, ".config");
        indexFile = new File(metadata, ".index");
        backupLogFile = new File(metadata, ".backuplog");
        try {
            loadConfig();
            loadIndex();
            loadBackupLog();
        } catch (IOException e) {
            throw new RuntimeException("error initializing index", e);
        }
    }

    public File getFile(UUID id) {
        File f = getRepositoryFile(id);
        return f.exists() ? f : null;
    }

    /**
     * @param id
     * @return stream of data in repository in its compressed form
     * @throws IOException
     */
    public InputStream get(UUID id) throws IOException {
        return new BufferedInputStream(new FileInputStream((getRepositoryFile(id))));
    }

    /**
     * directly adds data to repository without compressing it, assumes already compressed
     * 
     * @param id
     * @param input
     * @throws IOException
     */
    public void put(UUID id, InputStream input) throws IOException {
        IOUtils.copy(input, new FileOutputStream(getRepositoryFile(id)));
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

    public BackupLog getBackupLog() {
        return backupLog;
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public void saveIndex() throws IOException {
        saveToFile(indexFile, index);
    }
    
    public void saveBackupLog() throws IOException {
        saveToFile(backupLogFile, backupLog);
    }
    
    public void saveConfig() throws IOException {
        saveToFile(configFile, config);
    }
    
    private void loadIndex() throws IOException {
        index = loadFromFile(indexFile, new Index());
    }

    private void loadBackupLog() throws IOException {
        backupLog = loadFromFile(backupLogFile, new BackupLog());
    }
    
    private void loadConfig() throws IOException {
        config = loadFromFile(configFile, new Configuration(UUID.randomUUID()));        
    }
    
    @SuppressWarnings("unchecked")
    private <T> T loadFromFile(File f, T defaultValue) throws IOException {
        if (!f.exists() || f.length() == 0) {
            FileUtils.touch(f);
            return defaultValue;
        } else {
            return (T) JAXB.unmarshal(gzipInputStream(f), defaultValue.getClass());
        }           
    }
    
    private void saveToFile(File f, Object o) throws FileNotFoundException, IOException {
        OutputStream outputStream = gzipOutputStream(f);
        JAXB.marshal(o, outputStream);
        outputStream.flush();
        outputStream.close();
    }
    
    private InputStream gzipInputStream(File f) throws FileNotFoundException, IOException {
        return new GZIPInputStream(new FileInputStream(f), BUFFER_SIZE);
    }
    
    private OutputStream gzipOutputStream(File f) throws FileNotFoundException, IOException {
        return new GZIPOutputStream(new FileOutputStream(f), BUFFER_SIZE);
    }
}