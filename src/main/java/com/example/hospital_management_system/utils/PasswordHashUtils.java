package com.example.hospital_management_system.utils;

import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Logger;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordHashUtils {
    private static final Logger LOGGER = Logger.getLogger(PasswordHashUtils.class.getName());

    /**
     * Generate a hashed version of the plain password.
     *
     * @param plainPassword the plain password to be hashed
     * @return the hashed password
     */
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

    /**
     * Verify if the plain password matches the hashed one.
     *
     * @param plainPassword the plain password entered by the user
     * @param hashedPassword the stored hashed password from the database
     * @return true if the plain password matches the hashed one, false otherwise
     */
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

    /**
     * Check if a password is already hashed with BCrypt
     *
     * @param password the password to check
     * @return true if the password is already hashed, false otherwise
     */
    public static boolean isPasswordHashed(String password) {
        return password != null && password.startsWith("$2a$");
    }
}
