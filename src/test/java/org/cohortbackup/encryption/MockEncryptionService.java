package org.cohortbackup.encryption;

import java.io.InputStream;
import java.io.OutputStream;

public class MockEncryptionService implements EncryptionService {

    @Override
    public String encrypt(String data, String key) {
        return data;
    }

    @Override
    public InputStream encrypt(InputStream data, String key) {
        return data;
    }

    @Override
    public OutputStream encrypt(OutputStream dest, String key) {
        return dest;
    }

    @Override
    public String decrypt(String data, String key) {
        return data;
    }

    @Override
    public InputStream decrypt(InputStream data, String key) {
        return data;
    }
}