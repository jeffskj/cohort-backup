package org.cohortbackup.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

public class ChecksumUtilsTest {
    @Test
    public void testChecksums() {
        byte[] bytes = new byte[(int) FileUtils.ONE_MB];
        Arrays.fill(bytes, (byte) RandomUtils.nextInt());
        String digest = ChecksumUtils.getMD5Digest(bytes);
        for (int i = 0; i < 10; i++) {
            assertEquals(digest, ChecksumUtils.getMD5Digest(bytes));
        }
    }
}
