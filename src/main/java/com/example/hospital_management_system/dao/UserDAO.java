package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class UserDAO {

    /**
     * Registers a new user in the database.
     *
     * @param user the user object containing user details
     * @return the generated user ID or -1 if registration failed
     */
    public static int registerUser(Users user) {
        int generatedId = -1;
        String sql = "INSERT INTO users (user_name, user_email, user_password, user_phone, user_address, user_gender, role, profile) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getRole().name());

            // If no profile image is set, set it as null in the DB
            if (user.getProfile() == null) {
                stmt.setNull(8, java.sql.Types.BLOB);
            } else {
                stmt.setBytes(8, user.getProfile());
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            // Log the error for debugging purposes (e.g., using a logging framework)
            e.printStackTrace();
        }
        return generatedId;
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user to be fetched
     * @return the user object, or null if no user with the given email is found
     */
    public static Users getUserByEmail(String email) {
        Users user = null;
        String sql = "SELECT * FROM users WHERE user_email = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }

        } catch (Exception e) {
            // Log the error for debugging purposes (e.g., using a logging framework)
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user to be fetched
     * @return the user object, or null if no user with the given ID is found
     */
    public static Users getUserById(int id) {
        Users user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }

        } catch (Exception e) {
            // Log the error for debugging purposes (e.g., using a logging framework)
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Maps a ResultSet row to a Users object.
     *
     * @param rs the ResultSet containing the user data
     * @return a Users object populated with data from the ResultSet
     * @throws Exception if there is an issue accessing the ResultSet data
     */
    private static Users mapResultSetToUser(ResultSet rs) throws Exception {
        Users user = new Users();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("user_name"));
        user.setEmail(rs.getString("user_email"));
        user.setPassword(rs.getString("user_password"));
        user.setPhone(rs.getString("user_phone"));
        user.setAddress(rs.getString("user_address"));
        user.setGender(rs.getString("user_gender"));
        user.setRole(Users.Role.valueOf(rs.getString("role")));
        user.setProfile(rs.getBytes("profile"));

        return user;
    }
}
