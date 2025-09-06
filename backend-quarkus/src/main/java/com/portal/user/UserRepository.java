package com.portal.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public User findByEmail(String email) {
        return find("email", email.toLowerCase()).firstResult();
    }
    public User findByToken(String token) {
        return find("verificationToken", token).firstResult();
    }
}
