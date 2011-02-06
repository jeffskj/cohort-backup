package org.cohortbackup.encryption;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionServiceImpl implements EncryptionService
{
    private static final byte[] SALT = new byte[] { 0x7d, 0x60, 0x43, 0x5f, 0x02, (byte)0xe9, (byte)0xe0, (byte)0xae };
    private static final int ITERATION_COUNT = 512;
    
    private Map<Integer, Cipher> cipherCache = new HashMap<Integer, Cipher>();;
    
    @PostConstruct
    public void setup() {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }
    
    public String encrypt(String data, String key)
    {
        try
        {
            return IOUtils.toString(encrypt(IOUtils.toInputStream(data), key));
        }
        catch (IOException e) { throw new RuntimeException("this shouldn't be possible!"); }
    }
    
    public InputStream encrypt(InputStream data, String key)
    {
        return new CipherInputStream(data, getCipher(Cipher.ENCRYPT_MODE, key));
    }
    
    public OutputStream encrypt(OutputStream dest, String key)
    {
        return new CipherOutputStream(dest, getCipher(Cipher.ENCRYPT_MODE, key));
    }
    
    public String decrypt(String data, String key)
    {
        try
        {
            return IOUtils.toString(decrypt(IOUtils.toInputStream(data), key));
        }
        catch (IOException e) { throw new RuntimeException("this shouldn't be possible!"); }
    }
    
    public InputStream decrypt(InputStream data, String key)
    {
        return new CipherInputStream(data, getCipher(Cipher.DECRYPT_MODE, key));
    }

    private Cipher getCipher(int mode, String key)
    {
        if (cipherCache.containsKey(mode)) 
        {
            return cipherCache.get(mode);
        }
        
        try
        {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray(), SALT, ITERATION_COUNT);
            SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithSHAAnd128BitRC4", provider);
            
            Cipher cipher = Cipher.getInstance("ARC4", provider);
            cipher.init(mode, keyFact.generateSecret(keySpec), new PBEParameterSpec(SALT, ITERATION_COUNT));
            cipherCache.put(mode, cipher);
            
            return cipher;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new EncryptionException(e);
        }
        catch (NoSuchPaddingException e)
        {
            throw new EncryptionException(e);
        }
        catch (InvalidKeyException e)
        {
            throw new EncryptionException(e);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            throw new EncryptionException(e);
        }
        catch (InvalidKeySpecException e)
        {
            throw new EncryptionException(e);
        }        
    }
}
