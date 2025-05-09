package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.*;
import java.util.*;

public class AdminDAO {

    // Method to get patients with appointment count
    public List<Map<String, Object>> getPatientsWithAppointments() {
        List<Map<String, Object>> patientsList = new ArrayList<>();

        String query = "SELECT p.patient_id, u.user_name, u.user_email, COUNT(a.appointment_id) AS appointment_count " +
                "FROM patient p " +
                "JOIN users u ON p.user_id = u.user_id " +
                "LEFT JOIN appointment a ON p.patient_id = a.patient_id " +
                "GROUP BY p.patient_id, u.user_name, u.user_email";

        try (Connection connection = DBConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> patientData = new HashMap<>();
                patientData.put("patient_id", resultSet.getInt("patient_id"));
                patientData.put("user_name", resultSet.getString("user_name"));
                patientData.put("user_email", resultSet.getString("user_email"));
                patientData.put("appointment_count", resultSet.getInt("appointment_count"));
                patientsList.add(patientData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientsList;
    }

    // Method to get all doctors
    public List<Map<String, Object>> getAllDoctors() {
        List<Map<String, Object>> doctorsList = new ArrayList<>();

        String query = "SELECT d.doctor_id, u.user_name, d.specialty AS doctor_specialty, u.user_email " +
                "FROM doctor d " +
                "JOIN users u ON d.user_id = u.user_id";

        try (Connection connection = DBConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> doctorData = new HashMap<>();
                doctorData.put("doctor_id", resultSet.getInt("doctor_id"));
                doctorData.put("user_name", resultSet.getString("user_name"));
                doctorData.put("doctor_specialty", resultSet.getString("doctor_specialty"));
                doctorData.put("user_email", resultSet.getString("user_email"));
                doctorsList.add(doctorData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorsList;
    }

    // Method to get current user's details
    public Users getCurrentUser(int userId) {
        String query = "SELECT user_id, user_name, user_email FROM users WHERE user_id = ?";
        Users user = null;

        try (Connection connection = DBConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new Users();
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setName(resultSet.getString("user_name"));
                    user.setEmail(resultSet.getString("user_email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
