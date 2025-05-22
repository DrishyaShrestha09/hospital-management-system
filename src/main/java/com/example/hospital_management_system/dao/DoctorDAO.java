package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Doctor;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorDAO {
    private static final Logger LOGGER = Logger.getLogger(DoctorDAO.class.getName());

    public static boolean addDoctor(int userId, int experience, String specialty) {
        String sql = "INSERT INTO doctor (experience, specialty, user_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, experience);
            stmt.setString(2, specialty);
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding doctor", e);
        }
        return false;
    }

    public boolean updateDoctorProfile(Doctor doctor) {
        // Log the doctor object to debug
        LOGGER.info("Updating doctor: ID=" + doctor.getDoctorId() +
                ", Specialty=" + doctor.getSpecialty() +
                ", Experience=" + doctor.getExperience() +
                ", DepartmentID=" + doctor.getDepartmentId());

        if (doctor.getDoctorId() <= 0) {
            LOGGER.severe("Invalid doctor ID: " + doctor.getDoctorId());
            return false;
        }

        if (doctor.getDepartmentId() <= 0 || !isDepartmentExists(doctor.getDepartmentId())) {
            LOGGER.severe("Department ID " + doctor.getDepartmentId() + " does not exist or is invalid");
            return false;
        }

        if (!doctorExists(doctor.getDoctorId())) {
            LOGGER.severe("Doctor with ID " + doctor.getDoctorId() + " does not exist");
            return false;
        }

        String sql = "UPDATE doctor SET specialty = ?, experience = ?, department_id = ? WHERE doctor_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection()) {
            if (conn == null) {
                LOGGER.severe("Database connection is null");
                return false;
            }

            LOGGER.info("Database connection established successfully");

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, doctor.getSpecialty());
                stmt.setInt(2, doctor.getExperience());
                stmt.setInt(3, doctor.getDepartmentId());
                stmt.setInt(4, doctor.getDoctorId());

                LOGGER.info("Executing SQL: " + sql + " with params: [" +
                        doctor.getSpecialty() + ", " +
                        doctor.getExperience() + ", " +
                        doctor.getDepartmentId() + ", " +
                        doctor.getDoctorId() + "]");

                int rowsAffected = stmt.executeUpdate();
                LOGGER.info("Rows affected by update: " + rowsAffected);

                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating doctor profile: " + e.getMessage(), e);
            return false;
        }
    }

    private boolean doctorExists(int doctorId) {
        if (doctorId <= 0) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM doctor WHERE doctor_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if doctor exists", e);
        }

        return false;
    }

    private boolean isDepartmentExists(int departmentId) {
        if (departmentId <= 0) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM department WHERE department_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking department existence", e);
        }

        return false;
    }

    public Doctor getDoctorById(int doctorId) {
        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";
        Doctor doctor = null;

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    doctor = new Doctor();
                    doctor.setDoctorId(rs.getInt("doctor_id"));
                    doctor.setSpecialty(rs.getString("specialty"));
                    doctor.setExperience(rs.getInt("experience"));
                    doctor.setDepartmentId(rs.getInt("department_id"));
                } else {
                    LOGGER.warning("No doctor found with ID: " + doctorId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving doctor by ID", e);
        }

        return doctor;
    }

    public Doctor getDoctorByUserId(int userId) {
        String sql = "SELECT * FROM doctor WHERE user_id = ?";
        Doctor doctor = null;

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            LOGGER.info("Executing query to get doctor by user ID: " + userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    doctor = new Doctor();
                    doctor.setDoctorId(rs.getInt("doctor_id"));
                    doctor.setSpecialty(rs.getString("specialty"));
                    doctor.setExperience(rs.getInt("experience"));
                    doctor.setDepartmentId(rs.getInt("department_id"));
                    LOGGER.info("Found doctor with ID: " + doctor.getDoctorId() + " for user ID: " + userId);
                } else {
                    LOGGER.warning("No doctor found for user ID: " + userId);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving doctor by user ID", e);
        }

        return doctor;
    }
}
