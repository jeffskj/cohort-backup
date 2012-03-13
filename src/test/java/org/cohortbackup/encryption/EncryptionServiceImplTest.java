package org.cohortbackup.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class EncryptionServiceImplTest {
    private static final String KEY = "password";

    @Test
    public void testEncrypt() throws IOException {
        String input = "this is a test";

        EncryptionService encryptionService = new EncryptionServiceImpl();

        for (int i = 0; i < 100; i++) {
            String output = encryptionService.encrypt(input, KEY);
            assertFalse(input.equals(output));

            output = encryptionService.decrypt(output, KEY);
            assertEquals(input, output);
        }

        ByteArrayOutputStream dest = new ByteArrayOutputStream();
        IOUtils.write(input, encryptionService.encrypt(dest, KEY));
        assertEquals(input, encryptionService.decrypt(dest.toString(), KEY));
    }
}
