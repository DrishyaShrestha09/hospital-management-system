package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DoctorDAO {
    public static boolean addDoctor(int userId, int experience, String specialty) {
        String sql = "INSERT INTO doctor (experience, specialty, user_id) VALUES (?, ?, ?)"; // Removed department_id

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, experience);
            stmt.setString(2, specialty);
            stmt.setInt(3, userId); // Set user_id as before

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
