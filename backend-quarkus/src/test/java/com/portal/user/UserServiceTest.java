package com.portal.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class UserServiceTest {

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Test
    @TestTransaction
    void testRegisterAndAuthenticate() {
        String email = "serviceuser@example.com";
        String password = "StrongPass!";

        User user = userService.register(email, password);

        assertNotNull(user.id);
        assertEquals(email, user.email);
        assertFalse(user.emailVerified);

        assertTrue(userRepository.isPersistent(user));

        User auth = userService.authenticate(email, password);
        assertNotNull(auth);

        assertNull(userService.authenticate(email, "wrong"));
    }

    @Test
    @TestTransaction
    void testVerifyToken() {
        String email = "verify@example.com";
        String password = "TokenPass123";

        User user = userService.register(email, password);
        assertNotNull(user.verificationToken);

        boolean ok = userService.verifyToken(user.verificationToken);
        assertTrue(ok);

        User refreshed = userRepository.findByEmail(email);
        assertTrue(refreshed.emailVerified);
        assertNull(refreshed.verificationToken);
    }
}
