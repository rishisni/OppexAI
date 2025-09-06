package com.portal.security;

import java.util.HashMap;
import java.util.Map;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {
    public String issueToken(String email, boolean isVerified) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("verified", isVerified);
        return Jwt.issuer("portal-app")
                  .upn(email)
                  .claim("verified", isVerified)
                  .expiresIn(60 * 60) 
                  .sign();
    }
}
