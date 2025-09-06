package com.portal.user;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String email;

    @Column(nullable = false)
    public String passwordHash;

    @Column(nullable = false)
    public boolean emailVerified = false;

    @Column
    public String verificationToken;

    @Column(nullable = false)
    public Instant createdAt = Instant.now();
}
