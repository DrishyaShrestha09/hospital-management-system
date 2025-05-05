package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;
import com.example.hospital_management_system.utils.PasswordHashUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    /**
     * Register a new user in the database
     * @param user The user to register
     * @return The generated user ID, or -1 if registration failed
     */
    public static int registerUser(Users user) {
        String sql = "INSERT INTO users (user_name, user_email, user_password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            // Hash the password before storing
            String hashedPassword = PasswordHashUtils.hashPassword(user.getPassword());
            stmt.setString(3, hashedPassword);

            // Convert enum to string
            stmt.setString(4, user.getRole().toString().toLowerCase());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.warning("Creating user failed, no rows affected.");
                return -1;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    LOGGER.info("User registered successfully with ID: " + userId);
                    return userId;
                } else {
                    LOGGER.warning("Creating user failed, no ID obtained.");
                    return -1;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            return -1;
        }
    }

    /**
     * Get a user by their email address
     * @param email The email address to search for
     * @return The user if found, null otherwise
     */
    public static Users getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE user_email = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
        }

        return null;
    }

    /**
     * Get a user by their ID
     * @param userId The ID of the user to retrieve
     * @return The user if found, null otherwise
     */
    public static Users getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: " + userId, e);
        }

        return null;
    }

    /**
     * Update a user's information in the database
     * @param user The user with updated information
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateUser(Users user) {
        // Log the user object for debugging
        LOGGER.info("Updating user: ID=" + user.getUserId() +
                ", Name=" + user.getName() +
                ", Email=" + user.getEmail());

        // First check if user exists
        if (!userExists(user.getUserId())) {
            LOGGER.severe("User with ID " + user.getUserId() + " does not exist");
            return false;
        }

        // Get the current user from the database to compare
        Users currentUser = getUserById(user.getUserId());
        if (currentUser == null) {
            LOGGER.severe("Failed to retrieve current user data for ID: " + user.getUserId());
            return false;
        }

        // If password is not being changed, use the existing password
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
            LOGGER.info("Using existing password for user: " + user.getUserId());
        }

        // Use a simpler SQL query to avoid potential issues
        String sql = "UPDATE users SET user_name = ?, user_email = ?, user_password = ?, " +
                "user_phone = ?, user_address = ?, user_gender = ? WHERE user_id = ?";

        // If profile picture is provided, use a different query
        if (user.getProfile() != null && user.getProfile().length > 0) {
            sql = "UPDATE users SET user_name = ?, user_email = ?, user_password = ?, " +
                    "user_phone = ?, user_address = ?, user_gender = ?, profile = ? WHERE user_id = ?";
        }

        LOGGER.info("Executing SQL: " + sql);

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword()); // Password should already be hashed
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getGender());

            if (user.getProfile() != null && user.getProfile().length > 0) {
                stmt.setBytes(7, user.getProfile());
                stmt.setInt(8, user.getUserId());
            } else {
                stmt.setInt(7, user.getUserId());
            }

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Rows affected by update: " + rowsAffected);

            if (rowsAffected > 0) {
                LOGGER.info("User updated successfully: " + user.getUserId());
                return true;
            } else {
                LOGGER.warning("No rows affected when updating user: " + user.getUserId());
                return false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUserId() + ", Error: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if a user exists in the database
     * @param userId The ID of the user to check
     * @return true if the user exists, false otherwise
     */
    private static boolean userExists(int userId) {
        if (userId <= 0) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user exists", e);
        }

        return false;
    }

    /**
     * Map a ResultSet to a User object
     * @param rs The ResultSet containing user data
     * @return A populated User object
     * @throws SQLException If there's an error accessing the ResultSet
     */
    private static Users mapResultSetToUser(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("user_name"));
        user.setEmail(rs.getString("user_email"));

        // Set the password directly without hashing it again
        String dbPassword = rs.getString("user_password");
        if (dbPassword != null) {
            user.setPassword(dbPassword);
        }

        user.setPhone(rs.getString("user_phone"));
        user.setAddress(rs.getString("user_address"));
        user.setGender(rs.getString("user_gender"));

        // Convert role string to enum
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            try {
                user.setRole(Users.Role.valueOf(roleStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid role in database: " + roleStr);
            }
        }

        // Get profile picture if available
        byte[] profileData = rs.getBytes("profile");
        if (profileData != null) {
            user.setProfile(profileData);
        }

        return user;
    }
}
