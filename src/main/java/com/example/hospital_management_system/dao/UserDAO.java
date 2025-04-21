package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserDAO {

    // Register a new user (with hashed password)
    public static int registerUser(Users user) {
        String sql = "INSERT INTO users(user_name, user_email, user_password, user_phone, user_address, user_gender, role, profile) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnectionUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, hashedPassword);
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getGender());
            ps.setString(7, user.getRole().name());
            ps.setBytes(8, user.getProfile());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // return generated user ID
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while registering user: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return -1; // Registration failed
    }

    // Login a user (by checking hashed password)
    public static Users loginUser(String email, String plainPassword) {
        Users user = getUserByEmail(email);

        if (user != null && BCrypt.checkpw(plainPassword, user.getPassword())) {
            return user;
        }

        return null;
    }

    // Get user by email
    public static Users getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE user_email = ?";

        try (Connection con = DBConnectionUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching user by email: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return null;
    }

    // Get user by ID
    public static Users getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection con = DBConnectionUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error while fetching user by ID: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return null;
    }

    private static Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();

        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("user_name"));
        user.setEmail(rs.getString("user_email"));
        user.setPassword(rs.getString("user_password"));
        user.setPhone(rs.getString("user_phone"));
        user.setAddress(rs.getString("user_address"));
        user.setGender(rs.getString("user_gender"));

        // Convert string from DB to enum
        String roleStr = rs.getString("role");
        try {
            user.setRole(Users.Role.valueOf(roleStr.toLowerCase()));
        } catch (IllegalArgumentException e) {
            user.setRole(null); // or handle unknown roles
        }

        user.setProfile(rs.getBytes("profile"));

        return user;
    }
}
