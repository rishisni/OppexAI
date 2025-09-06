package com.portal.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PasswordUtilsTest {

    @Test
    void testHashAndVerify() {
        String raw = "MySecret123!";
        String hash = PasswordUtils.hash(raw);

        assertNotNull(hash);
        assertNotEquals(raw, hash); 
        assertTrue(PasswordUtils.verify(raw, hash));
        assertFalse(PasswordUtils.verify("wrong", hash));
    }
}
