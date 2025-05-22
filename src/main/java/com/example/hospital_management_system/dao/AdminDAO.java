package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDAO {
    private static final Logger LOGGER = Logger.getLogger(AdminDAO.class.getName());

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

        String query = "SELECT d.doctor_id, u.user_name, d.specialty AS doctor_specialty, u.user_email, u.user_id " +
                "FROM doctor d " +
                "JOIN users u ON d.user_id = u.user_id";

        try (Connection connection = DBConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Map<String, Object> doctorData = new HashMap<>();
                doctorData.put("doctor_id", resultSet.getInt("doctor_id"));
                doctorData.put("user_id", resultSet.getInt("user_id"));
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

    public boolean updateDoctor(int doctorId, String name, String specialty, String email) {
        Connection conn = null;
        boolean success = false;

        try {
            conn = DBConnectionUtils.getConnection();
            conn.setAutoCommit(false);
            LOGGER.info("Starting transaction to update doctor ID: " + doctorId);

            // First, get the user_id associated with this doctor
            int userId = getUserIdByDoctorId(doctorId);
            if (userId <= 0) {
                LOGGER.severe("Could not find user ID for doctor ID: " + doctorId);
                return false;
            }

            LOGGER.info("Found user ID: " + userId + " for doctor ID: " + doctorId);

            // Update the user information
            String updateUserSql = "UPDATE users SET user_name = ?, user_email = ? WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateUserSql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setInt(3, userId);
                int userRowsUpdated = stmt.executeUpdate();

                if (userRowsUpdated <= 0) {
                    LOGGER.severe("Failed to update user record for user ID: " + userId);
                    conn.rollback();
                    return false;
                }
                LOGGER.info("Updated user record for user ID: " + userId);
            }

            // Update the doctor specialty
            String updateDoctorSql = "UPDATE doctor SET specialty = ? WHERE doctor_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateDoctorSql)) {
                stmt.setString(1, specialty);
                stmt.setInt(2, doctorId);
                int doctorRowsUpdated = stmt.executeUpdate();

                if (doctorRowsUpdated <= 0) {
                    LOGGER.severe("Failed to update doctor record for doctor ID: " + doctorId);
                    conn.rollback();
                    return false;
                }
                LOGGER.info("Updated doctor record for doctor ID: " + doctorId);
            }

            // Commit the transaction
            conn.commit();
            LOGGER.info("Transaction committed successfully - doctor information updated");
            success = true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error during doctor update: " + e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.info("Transaction rolled back due to error");
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    LOGGER.info("Connection closed and auto-commit restored");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing connection", e);
                }
            }
        }

        return success;
    }

    public boolean deleteDoctor(int doctorId) {
        int userId = getUserIdByDoctorId(doctorId);
        if (userId <= 0) {
            LOGGER.severe("Could not find user ID for doctor ID: " + doctorId);
            return false;
        }

        LOGGER.info("Starting complete deletion process for doctor ID: " + doctorId + " with user ID: " + userId);

        Connection conn = null;
        boolean success = false;

        try {
            conn = DBConnectionUtils.getConnection();

            conn.setAutoCommit(false);
            LOGGER.info("Transaction started - auto-commit disabled");

            // Print the current database state for debugging
            printDoctorInfo(conn, doctorId, userId);

            String checkAppointmentStatusSql =
                    "SELECT COUNT(*) FROM appointment_status WHERE appointment_id IN " +
                            "(SELECT appointment_id FROM appointment WHERE doctor_id = ?)";

            try (PreparedStatement stmt = conn.prepareStatement(checkAppointmentStatusSql)) {
                stmt.setInt(1, doctorId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    LOGGER.info("Found " + rs.getInt(1) + " appointment status records to delete for doctor ID: " + doctorId);

                    // Delete appointment status records
                    String deleteAppointmentStatusSql =
                            "DELETE FROM appointment_status WHERE appointment_id IN " +
                                    "(SELECT appointment_id FROM appointment WHERE doctor_id = ?)";

                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteAppointmentStatusSql)) {
                        deleteStmt.setInt(1, doctorId);
                        int statusRowsDeleted = deleteStmt.executeUpdate();
                        LOGGER.info("Deleted " + statusRowsDeleted + " appointment status records");
                    }
                }
            }

            String deleteAppointmentsSql = "DELETE FROM appointment WHERE doctor_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteAppointmentsSql)) {
                stmt.setInt(1, doctorId);
                int appointmentsDeleted = stmt.executeUpdate();
                LOGGER.info("Deleted " + appointmentsDeleted + " appointments for doctor ID: " + doctorId);
            }

            String deleteDoctorSql = "DELETE FROM doctor WHERE doctor_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteDoctorSql)) {
                stmt.setInt(1, doctorId);
                int doctorRowsDeleted = stmt.executeUpdate();

                if (doctorRowsDeleted <= 0) {
                    LOGGER.severe("No doctor record was deleted for doctor ID: " + doctorId);
                    conn.rollback();
                    return false;
                }
                LOGGER.info("Deleted doctor record for doctor ID: " + doctorId);
            }

            String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                stmt.setInt(1, userId);
                int userRowsDeleted = stmt.executeUpdate();

                if (userRowsDeleted <= 0) {
                    LOGGER.severe("Failed to delete user record for user ID: " + userId);
                    conn.rollback();
                    return false;
                }
                LOGGER.info("Completely deleted user record for user ID: " + userId);
            }

            if (verifyDoctorDeleted(conn, doctorId, userId)) {
                conn.commit();
                LOGGER.info("Transaction committed successfully - doctor completely deleted from system");
                success = true;
            } else {
                LOGGER.severe("Verification failed - doctor or user still exists in database");
                conn.rollback();
                success = false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error during doctor deletion: " + e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                    LOGGER.info("Transaction rolled back due to error");
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    LOGGER.info("Connection closed and auto-commit restored");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing connection", e);
                }
            }
        }

        return success;
    }

    private void printDoctorInfo(Connection conn, int doctorId, int userId) throws SQLException {
        String checkDoctorSql = "SELECT * FROM doctor WHERE doctor_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkDoctorSql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LOGGER.info("BEFORE DELETE - Doctor record exists: doctor_id=" + rs.getInt("doctor_id") +
                        ", user_id=" + rs.getInt("user_id") +
                        ", specialty=" + rs.getString("specialty"));
            } else {
                LOGGER.info("BEFORE DELETE - No doctor record found with ID: " + doctorId);
            }
        }

        // Check user record
        String checkUserSql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkUserSql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LOGGER.info("BEFORE DELETE - User record exists: user_id=" + rs.getInt("user_id") +
                        ", name=" + rs.getString("user_name") +
                        ", email=" + rs.getString("user_email") +
                        ", role=" + rs.getString("role"));
            } else {
                LOGGER.info("BEFORE DELETE - No user record found with ID: " + userId);
            }
        }
    }

    private boolean verifyDoctorDeleted(Connection conn, int doctorId, int userId) throws SQLException {
        boolean doctorDeleted = true;
        boolean userDeleted = true;

        // Check doctor record
        String checkDoctorSql = "SELECT COUNT(*) FROM doctor WHERE doctor_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkDoctorSql)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                LOGGER.severe("VERIFICATION FAILED - Doctor record still exists with ID: " + doctorId);
                doctorDeleted = false;
            } else {
                LOGGER.info("VERIFICATION PASSED - Doctor record successfully deleted with ID: " + doctorId);
            }
        }

        // Check user record
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(checkUserSql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                LOGGER.severe("VERIFICATION FAILED - User record still exists with ID: " + userId);
                userDeleted = false;
            } else {
                LOGGER.info("VERIFICATION PASSED - User record successfully deleted with ID: " + userId);
            }
        }

        return doctorDeleted && userDeleted;
    }

    private int getUserIdByDoctorId(int doctorId) {
        String sql = "SELECT user_id FROM doctor WHERE doctor_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    LOGGER.info("Found user ID: " + userId + " for doctor ID: " + doctorId);
                    return userId;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user ID by doctor ID", e);
        }

        LOGGER.warning("No user ID found for doctor ID: " + doctorId);
        return -1;
    }
}