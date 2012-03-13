package org.cohortbackup.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.apache.commons.io.IOUtils;

public class ChecksumUtils {
    static final byte[] HEX_CHAR_TABLE = {(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
            (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
            (byte) 'f'};

    public static String getMD5Digest(File f) {
        InputStream is = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            is = new DigestInputStream(new BufferedInputStream(new FileInputStream(f)), md);
            byte[] bytes = new byte[1024];
            while (is.available() > 0) {
                is.read(bytes);
            }
            return getHexString(md.digest());
        } catch (Exception e) {
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static String getMD5Digest(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return getHexString(md.digest(bytes));
        } catch (Exception e) {
            return null;
        }
    }

    private static String getHexString(byte[] raw) throws UnsupportedEncodingException {
        byte[] hex = new byte[2 * raw.length];
        int index = 0;
        for (byte b : raw) {
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE[v & 0xF];
        }
        return new String(hex, "ASCII");
    }
}
