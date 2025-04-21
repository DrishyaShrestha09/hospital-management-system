package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Doctor;
import java.sql.*;

public class DoctorDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/hospitalmanagement";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "";

    // Establish connection to database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // Update doctor profile
    public boolean updateDoctorProfile(Doctor doctor) {
        String sql = "UPDATE doctor SET specialty = ?, experience = ?, department_id = ? WHERE doctor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getSpecialty());
            stmt.setInt(2, doctor.getExperience());
            stmt.setInt(3, doctor.getDepartmentId());
            stmt.setInt(4, doctor.getDoctorId());

            int rows = stmt.executeUpdate();
            System.out.println("Rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get doctor by doctor_id
    public Doctor getDoctorById(int doctorId) {
        Doctor doctor = null;
        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setSpecialty(rs.getString("specialty"));
                doctor.setExperience(rs.getInt("experience"));
                doctor.setDepartmentId(rs.getInt("department_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }
}
