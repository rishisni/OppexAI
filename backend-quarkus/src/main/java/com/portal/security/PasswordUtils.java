package com.portal.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtils {
    private static final int COST = 12;
    public static String hash(String plain) {
        return BCrypt.withDefaults().hashToString(COST, plain.toCharArray());
    }
    public static boolean verify(String plain, String hashed) {
        return BCrypt.verifyer().verify(plain.toCharArray(), hashed).verified;
    }
}
