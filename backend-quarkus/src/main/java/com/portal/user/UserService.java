package com.portal.user;

import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.portal.security.PasswordUtils;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    @Inject
    UserRepository repo;

    @Inject
    Mailer mailer;

    @ConfigProperty(name = "app.backend.base-url")
    String backendBaseUrl;

    @Transactional
    public User register(String email, String rawPassword) {
        String norm = email.trim().toLowerCase();

        if (repo.findByEmail(norm) != null) {
            throw new IllegalArgumentException("Email already registered");
        }

        User u = new User();
        u.email = norm;
        u.passwordHash = PasswordUtils.hash(rawPassword);
        u.emailVerified = false;
        u.verificationToken = UUID.randomUUID().toString().replace("-", "");
        repo.persist(u);

        String link = backendBaseUrl + "/api/verify/" + u.verificationToken;

        LOG.infof("Signup success: sending verification email to %s with link %s", u.email, link);

        try {
            String body = "Hi " + u.email + ",\n\n"
                    + "Please click the link below to verify your account:\n\n"
                    + link + "\n\n"
                    + "After verifying, you will be redirected to login.\n\n"
                    + "Thanks,\nPortal Team";

            mailer.send(Mail.withText(u.email, "Verify your account", body));
        } catch (Exception e) {
            LOG.error("Failed to send verification email", e);
        }

        return u;
    }

    public User authenticate(String email, String password) {
        String norm = email.trim().toLowerCase();
        User u = repo.findByEmail(norm);

        if (u == null) {
            LOG.warnf("Login failed: no account found for %s", norm);
            return null;
        }

        if (!PasswordUtils.verify(password, u.passwordHash)) {
            LOG.warnf("Login failed: invalid password for %s", norm);
            return null;
        }

        LOG.infof("Login success: %s (verified=%s)", u.email, u.emailVerified);
        return u;
    }

    @Transactional
    public boolean verifyToken(String token) {
        User u = repo.findByToken(token);
        if (u == null) {
            LOG.warnf("Email verification failed: invalid token %s", token);
            return false;
        }

        u.emailVerified = true;
        u.verificationToken = null;
        LOG.infof("Email verified for %s", u.email);
        return true;
    }
}
