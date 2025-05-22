package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Patient;
import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientProfileDAO {
    private static final Logger LOGGER = Logger.getLogger(PatientProfileDAO.class.getName());

    public static boolean addPatient(int userId) {
        String sql = "INSERT INTO patient (user_id) VALUES (?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding patient", e);
        }
        return false;
    }

    public boolean updatePatientBasicInfo(Users user) {
        String sql = "UPDATE users SET user_name = ?, user_phone = ?, user_address = ?, user_gender = ? WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getGender());
            stmt.setInt(5, user.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient basic info", e);
            return false;
        }
    }

    public boolean updatePatientMedicalInfo(int userId, String dateOfBirth, String bloodGroup,
                                            String emergencyContact, String allergies, String medicalConditions) {
        // First check if the record exists
        String checkSql = "SELECT patient_id FROM patient WHERE user_id = ?";
        String insertSql = "INSERT INTO patient_medical_info (patient_id, date_of_birth, blood_group, emergency_contact, allergies, medical_conditions) VALUES (?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE patient_medical_info SET date_of_birth = ?, blood_group = ?, emergency_contact = ?, allergies = ?, medical_conditions = ? WHERE patient_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int patientId = rs.getInt("patient_id");

                // Check if medical info exists
                String checkMedicalSql = "SELECT 1 FROM patient_medical_info WHERE patient_id = ?";
                try (PreparedStatement checkMedicalStmt = conn.prepareStatement(checkMedicalSql)) {
                    checkMedicalStmt.setInt(1, patientId);
                    ResultSet medicalRs = checkMedicalStmt.executeQuery();

                    if (medicalRs.next()) {
                        // Update existing record
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, dateOfBirth);
                            updateStmt.setString(2, bloodGroup);
                            updateStmt.setString(3, emergencyContact);
                            updateStmt.setString(4, allergies);
                            updateStmt.setString(5, medicalConditions);
                            updateStmt.setInt(6, patientId);

                            return updateStmt.executeUpdate() > 0;
                        }
                    } else {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setInt(1, patientId);
                            insertStmt.setString(2, dateOfBirth);
                            insertStmt.setString(3, bloodGroup);
                            insertStmt.setString(4, emergencyContact);
                            insertStmt.setString(5, allergies);
                            insertStmt.setString(6, medicalConditions);

                            return insertStmt.executeUpdate() > 0;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating patient medical info", e);
        }

        return false;
    }

    public java.util.Map<String, String> getPatientMedicalInfo(int userId) {
        java.util.Map<String, String> medicalInfo = new java.util.HashMap<>();

        String sql = "SELECT pmi.* FROM patient_medical_info pmi " +
                "JOIN patient p ON pmi.patient_id = p.patient_id " +
                "WHERE p.user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medicalInfo.put("dateOfBirth", rs.getString("date_of_birth"));
                medicalInfo.put("bloodGroup", rs.getString("blood_group"));
                medicalInfo.put("emergencyContact", rs.getString("emergency_contact"));
                medicalInfo.put("allergies", rs.getString("allergies"));
                medicalInfo.put("medicalConditions", rs.getString("medical_conditions"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving patient medical info", e);
        }

        return medicalInfo;
    }
}