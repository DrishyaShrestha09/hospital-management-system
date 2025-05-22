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

    public static int registerUser(Users user) {
        String sql = "INSERT INTO users (user_name, user_email, user_password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            // Ensure the password is hashed before storing
            String passwordToStore = user.getPassword();
            if (!PasswordHashUtils.isPasswordHashed(passwordToStore)) {
                passwordToStore = PasswordHashUtils.hashPassword(passwordToStore);
                LOGGER.info("Password hashed during registration");
            }
            stmt.setString(3, passwordToStore);

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

    public static Users getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE user_email = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Users user = mapResultSetToUser(rs);
                LOGGER.info("User found by email: " + email);
                return user;
            } else {
                LOGGER.info("No user found with email: " + email);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by email: " + email, e);
        }

        return null;
    }

    public static Users getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Users user = mapResultSetToUser(rs);
                LOGGER.info("User found by ID: " + userId);
                return user;
            } else {
                LOGGER.info("No user found with ID: " + userId);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user by ID: " + userId, e);
        }

        return null;
    }

    public static boolean updateUser(Users user) {
        LOGGER.info("Updating user: ID=" + user.getUserId() +
                ", Name=" + user.getName() +
                ", Email=" + user.getEmail());

        if (!userExists(user.getUserId())) {
            LOGGER.severe("User with ID " + user.getUserId() + " does not exist");
            return false;
        }

        Users currentUser = getUserById(user.getUserId());
        if (currentUser == null) {
            LOGGER.severe("Failed to retrieve current user data for ID: " + user.getUserId());
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(currentUser.getPassword());
            LOGGER.info("Using existing password for user: " + user.getUserId());
        }

        String sql = "UPDATE users SET user_name = ?, user_email = ?, user_password = ?, " +
                "user_phone = ?, user_address = ?, user_gender = ? WHERE user_id = ?";

        if (user.getProfile() != null && user.getProfile().length > 0) {
            sql = "UPDATE users SET user_name = ?, user_email = ?, user_password = ?, " +
                    "user_phone = ?, user_address = ?, user_gender = ?, profile = ? WHERE user_id = ?";
        }

        LOGGER.info("Executing SQL: " + sql);

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());

            String passwordToStore = user.getPassword();
            if (!PasswordHashUtils.isPasswordHashed(passwordToStore)) {
                passwordToStore = PasswordHashUtils.hashPassword(passwordToStore);
                LOGGER.info("Password hashed before update for user: " + user.getUserId());
            }
            stmt.setString(3, passwordToStore);

            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getGender());

            if (user.getProfile() != null && user.getProfile().length > 0) {
                stmt.setBytes(7, user.getProfile());
                stmt.setInt(8, user.getUserId());
            } else {
                stmt.setInt(7, user.getUserId());
            }

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