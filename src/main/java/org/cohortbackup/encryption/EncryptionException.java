package org.cohortbackup.encryption;

public class EncryptionException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public EncryptionException(Throwable t)
    {
        super(t);
    }
}
