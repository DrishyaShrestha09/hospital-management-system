package com.example.hospital_management_system.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordHashUtils {

    /**
     * Generate a hashed version of the plain password.
     *
     * @param plainPassword the plain password to be hashed
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        // Salt and hash the plain password with a work factor of 12
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
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
            return false;
        }
        // Check if the plain password matches the hashed password
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
