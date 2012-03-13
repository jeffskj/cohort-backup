package org.cohortbackup.encryption;

import java.io.InputStream;
import java.io.OutputStream;

public interface EncryptionService {
    String encrypt(String data, String key);

    InputStream encrypt(InputStream data, String key);

    OutputStream encrypt(OutputStream dest, String key);

    String decrypt(String data, String key);

    InputStream decrypt(InputStream data, String key);
}