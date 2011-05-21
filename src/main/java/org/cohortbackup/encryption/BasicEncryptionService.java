package org.cohortbackup.encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.enterprise.inject.Alternative;

import org.apache.commons.io.IOUtils;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

@Alternative
public class BasicEncryptionService implements EncryptionService {
    
    @Override
    public String encrypt(String data, String key) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor(); 
        encryptor.setPassword(key);
        return encryptor.encrypt(key);
    }

    @Override
    public InputStream encrypt(InputStream data, String key) {
        BasicBinaryEncryptor encryptor = new BasicBinaryEncryptor();
        encryptor.setPassword(key);
        try {
            return new ByteArrayInputStream(encryptor.encrypt(IOUtils.toByteArray(data)));
        } catch (IOException e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public OutputStream encrypt(OutputStream dest, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String decrypt(String data, String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream decrypt(InputStream data, String key) {
        throw new UnsupportedOperationException();
    }

}
