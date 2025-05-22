package com.example.hospital_management_system.utils;

import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Logger;

public class PasswordHashUtils {
    private static final Logger LOGGER = Logger.getLogger(PasswordHashUtils.class.getName());


    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            LOGGER.warning("Attempted to hash null or empty password");
            return null;
        }

        try {
            // Salt and hash the plain password with a work factor of 12
            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
            LOGGER.fine("Password hashed successfully");
            return hashedPassword;
        } catch (Exception e) {
            LOGGER.severe("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            LOGGER.warning("Attempted to verify with null password");
            return false;
        }

        try {
            // Check if the plain password matches the hashed password
            boolean result = BCrypt.checkpw(plainPassword, hashedPassword);
            LOGGER.fine("Password verification result: " + result);
            return result;
        } catch (Exception e) {
            LOGGER.severe("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    public static boolean isPasswordHashed(String password) {
        return password != null && password.startsWith("$2a$");
    }
}
