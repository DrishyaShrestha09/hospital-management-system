package com.example.hospital_management_system.services;

import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentService {

    private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());
    private static AppointmentService instance;

    private AppointmentService() {}

    public static AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    /**
     * Get appointments for a specific patient (patientId from session)
     */
    public List<Map<String, String>> getAppointmentsForPatient(int userId) {
        List<Map<String, String>> appointments = new ArrayList<>();

        // First get the patient_id from user_id
        String getPatientIdSql = "SELECT patient_id FROM patient WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement patientStmt = conn.prepareStatement(getPatientIdSql)) {

            patientStmt.setInt(1, userId);
            ResultSet patientRs = patientStmt.executeQuery();

            if (patientRs.next()) {
                int patientId = patientRs.getInt("patient_id");

                // Now get the appointments
                String sql = "SELECT a.appointment_id, u.user_name AS doctorName, a.appointment_date, a.time_slot, a.cause, " +
                        "COALESCE(s.status, 'pending') AS status " +
                        "FROM appointment a " +
                        "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                        "JOIN users u ON d.user_id = u.user_id " +
                        "LEFT JOIN appointment_status s ON a.appointment_id = s.appointment_id " +
                        "WHERE a.patient_id = ? " +
                        "ORDER BY a.appointment_date DESC";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, patientId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        Map<String, String> appointment = new HashMap<>();
                        appointment.put("id", String.valueOf(rs.getInt("appointment_id")));
                        appointment.put("doctorName", rs.getString("doctorName"));
                        appointment.put("date", rs.getString("appointment_date"));
                        appointment.put("timeSlot", rs.getString("time_slot"));
                        appointment.put("cause", rs.getString("cause"));
                        appointment.put("status", rs.getString("status"));

                        appointments.add(appointment);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching patient appointments", e);
        }

        return appointments;
    }

    /**
     * Get appointments for a doctor based on userId from session
     */
    public List<Map<String, String>> getAppointmentsForDoctor(int userId) {
        List<Map<String, String>> appointments = new ArrayList<>();

        String getDoctorIdSql = "SELECT doctor_id FROM doctor WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement getDoctorIdStmt = conn.prepareStatement(getDoctorIdSql)) {

            getDoctorIdStmt.setInt(1, userId);
            ResultSet doctorRs = getDoctorIdStmt.executeQuery();

            if (doctorRs.next()) {
                int doctorId = doctorRs.getInt("doctor_id");

                // Fetch appointments using doctorId
                String appointmentSql = "SELECT a.appointment_id, u.user_name AS patientName, a.appointment_date, a.time_slot, a.cause, " +
                        "COALESCE(s.status, 'pending') AS status " +
                        "FROM appointment a " +
                        "JOIN patient p ON a.patient_id = p.patient_id " +
                        "JOIN users u ON p.user_id = u.user_id " +
                        "LEFT JOIN appointment_status s ON a.appointment_id = s.appointment_id " +
                        "WHERE a.doctor_id = ? " +
                        "ORDER BY a.appointment_date DESC";

                try (PreparedStatement appointmentStmt = conn.prepareStatement(appointmentSql)) {
                    appointmentStmt.setInt(1, doctorId);
                    ResultSet appointmentRs = appointmentStmt.executeQuery();

                    // Clear the list before populating it with new data
                    appointments.clear();

                    while (appointmentRs.next()) {
                        Map<String, String> appointment = new HashMap<>();
                        int appointmentId = appointmentRs.getInt("appointment_id");
                        appointment.put("id", String.valueOf(appointmentId));
                        appointment.put("patientName", appointmentRs.getString("patientName"));
                        appointment.put("date", appointmentRs.getString("appointment_date"));
                        appointment.put("timeSlot", appointmentRs.getString("time_slot"));
                        appointment.put("cause", appointmentRs.getString("cause"));
                        appointment.put("status", appointmentRs.getString("status"));

                        appointments.add(appointment);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctor appointments", e);
        }

        return appointments;
    }

    /**
     * Get detailed information for a specific appointment
     */
    public Map<String, String> getAppointmentDetails(int appointmentId) {
        Map<String, String> appointmentDetails = new HashMap<>();

        String sql = "SELECT a.appointment_id, u.user_name AS patientName, a.appointment_date, a.time_slot, a.cause, " +
                "d.specialty, du.user_name AS doctorName, COALESCE(s.status, 'pending') AS status, s.notes " +
                "FROM appointment a " +
                "JOIN patient p ON a.patient_id = p.patient_id " +
                "JOIN users u ON p.user_id = u.user_id " +
                "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                "JOIN users du ON d.user_id = du.user_id " +
                "LEFT JOIN appointment_status s ON a.appointment_id = s.appointment_id " +
                "WHERE a.appointment_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                appointmentDetails.put("appointmentId", String.valueOf(rs.getInt("appointment_id")));
                appointmentDetails.put("patientName", rs.getString("patientName"));
                appointmentDetails.put("date", rs.getString("appointment_date"));
                appointmentDetails.put("timeSlot", rs.getString("time_slot"));
                appointmentDetails.put("cause", rs.getString("cause"));
                appointmentDetails.put("specialty", rs.getString("specialty"));
                appointmentDetails.put("doctorName", rs.getString("doctorName"));
                appointmentDetails.put("status", rs.getString("status"));

                String notes = rs.getString("notes");
                if (notes != null) {
                    appointmentDetails.put("notes", notes);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching appointment details", e);
        }

        return appointmentDetails;
    }

    /**
     * Cancel an appointment by deleting it from the database
     * @param appointmentId The ID of the appointment to cancel
     * @param userId The ID of the user making the cancellation request
     * @return true if cancellation was successful, false otherwise
     */
    public boolean cancelAppointment(int appointmentId, int userId) {
        // First get the patient_id for this user
        String getPatientIdSql = "SELECT patient_id FROM patient WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement patientStmt = conn.prepareStatement(getPatientIdSql)) {

            patientStmt.setInt(1, userId);
            ResultSet patientRs = patientStmt.executeQuery();

            if (patientRs.next()) {
                int patientId = patientRs.getInt("patient_id");

                // Delete the appointment directly
                String deleteSql = "DELETE FROM appointment WHERE appointment_id = ? AND patient_id = ?";

                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, appointmentId);
                    deleteStmt.setInt(2, patientId);

                    int rowsAffected = deleteStmt.executeUpdate();
                    return rowsAffected > 0;
                }
            }

            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling appointment", e);
            return false;
        }
    }

    /**
     * Update the status of an appointment
     * @param appointmentId The ID of the appointment to update
     * @param status The new status (confirmed, cancelled, completed)
     * @param doctorUserId The user ID of the doctor making the update
     * @return true if update was successful, false otherwise
     */
    public boolean updateAppointmentStatus(int appointmentId, String status, int doctorUserId) {
        // First get the doctor_id for this user
        String getDoctorIdSql = "SELECT doctor_id FROM doctor WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement doctorStmt = conn.prepareStatement(getDoctorIdSql)) {

            doctorStmt.setInt(1, doctorUserId);
            ResultSet doctorRs = doctorStmt.executeQuery();

            if (doctorRs.next()) {
                int doctorId = doctorRs.getInt("doctor_id");

                // Check if this appointment belongs to this doctor
                String checkSql = "SELECT appointment_id FROM appointment WHERE appointment_id = ? AND doctor_id = ?";

                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, appointmentId);
                    checkStmt.setInt(2, doctorId);
                    ResultSet checkRs = checkStmt.executeQuery();

                    if (checkRs.next()) {
                        // Check if status already exists for this appointment
                        String checkStatusSql = "SELECT status_id FROM appointment_status WHERE appointment_id = ?";

                        try (PreparedStatement checkStatusStmt = conn.prepareStatement(checkStatusSql)) {
                            checkStatusStmt.setInt(1, appointmentId);
                            ResultSet checkStatusRs = checkStatusStmt.executeQuery();

                            if (checkStatusRs.next()) {
                                // Update existing status
                                String updateSql = "UPDATE appointment_status SET status = ?, updated_by = ?, updated_at = NOW() WHERE appointment_id = ?";

                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setString(1, status);
                                    updateStmt.setInt(2, doctorUserId);
                                    updateStmt.setInt(3, appointmentId);

                                    int rowsAffected = updateStmt.executeUpdate();
                                    return rowsAffected > 0;
                                }
                            } else {
                                // Insert new status
                                String insertSql = "INSERT INTO appointment_status (appointment_id, status, updated_by) VALUES (?, ?, ?)";

                                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                    insertStmt.setInt(1, appointmentId);
                                    insertStmt.setString(2, status);
                                    insertStmt.setInt(3, doctorUserId);

                                    int rowsAffected = insertStmt.executeUpdate();
                                    return rowsAffected > 0;
                                }
                            }
                        }
                    } else {
                        LOGGER.log(Level.WARNING, "Attempted to update appointment that doesn't belong to this doctor");
                        return false;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating appointment status", e);
            return false;
        }
    }

    /**
     * Update appointment status with notes
     * @param appointmentId The ID of the appointment to update
     * @param status The new status (confirmed, cancelled, completed)
     * @param notes Additional notes for the appointment
     * @param doctorUserId The user ID of the doctor making the update
     * @return true if update was successful, false otherwise
     */
    public boolean updateAppointmentStatusWithNotes(int appointmentId, String status, String notes, int doctorUserId) {
        // First get the doctor_id for this user
        String getDoctorIdSql = "SELECT doctor_id FROM doctor WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement doctorStmt = conn.prepareStatement(getDoctorIdSql)) {

            doctorStmt.setInt(1, doctorUserId);
            ResultSet doctorRs = doctorStmt.executeQuery();

            if (doctorRs.next()) {
                int doctorId = doctorRs.getInt("doctor_id");

                // Check if this appointment belongs to this doctor
                String checkSql = "SELECT appointment_id FROM appointment WHERE appointment_id = ? AND doctor_id = ?";

                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, appointmentId);
                    checkStmt.setInt(2, doctorId);
                    ResultSet checkRs = checkStmt.executeQuery();

                    if (checkRs.next()) {
                        // Check if status already exists for this appointment
                        String checkStatusSql = "SELECT status_id FROM appointment_status WHERE appointment_id = ?";

                        try (PreparedStatement checkStatusStmt = conn.prepareStatement(checkStatusSql)) {
                            checkStatusStmt.setInt(1, appointmentId);
                            ResultSet checkStatusRs = checkStatusStmt.executeQuery();

                            if (checkStatusRs.next()) {
                                // Update existing status
                                String updateSql = "UPDATE appointment_status SET status = ?, notes = ?, updated_by = ?, updated_at = NOW() WHERE appointment_id = ?";

                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setString(1, status);
                                    updateStmt.setString(2, notes);
                                    updateStmt.setInt(3, doctorUserId);
                                    updateStmt.setInt(4, appointmentId);

                                    int rowsAffected = updateStmt.executeUpdate();
                                    return rowsAffected > 0;
                                }
                            } else {
                                // Insert new status
                                String insertSql = "INSERT INTO appointment_status (appointment_id, status, notes, updated_by) VALUES (?, ?, ?, ?)";

                                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                    insertStmt.setInt(1, appointmentId);
                                    insertStmt.setString(2, status);
                                    insertStmt.setString(3, notes);
                                    insertStmt.setInt(4, doctorUserId);

                                    int rowsAffected = insertStmt.executeUpdate();
                                    return rowsAffected > 0;
                                }
                            }
                        }
                    } else {
                        LOGGER.log(Level.WARNING, "Attempted to update appointment that doesn't belong to this doctor");
                        return false;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating appointment status with notes", e);
            return false;
        }
    }

    /**
     * Get appointment counts by status for a doctor
     * @param doctorUserId The user ID of the doctor
     * @return Map containing counts for each status
     */
    public Map<String, Integer> getAppointmentCountsByStatus(int doctorUserId) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("pending", 0);
        counts.put("confirmed", 0);
        counts.put("cancelled", 0);
        counts.put("completed", 0);
        counts.put("total", 0);

        // First get the doctor_id for this user
        String getDoctorIdSql = "SELECT doctor_id FROM doctor WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement doctorStmt = conn.prepareStatement(getDoctorIdSql)) {

            doctorStmt.setInt(1, doctorUserId);
            ResultSet doctorRs = doctorStmt.executeQuery();

            if (doctorRs.next()) {
                int doctorId = doctorRs.getInt("doctor_id");

                // Get total count
                String totalSql = "SELECT COUNT(*) AS total FROM appointment WHERE doctor_id = ?";
                try (PreparedStatement totalStmt = conn.prepareStatement(totalSql)) {
                    totalStmt.setInt(1, doctorId);
                    ResultSet totalRs = totalStmt.executeQuery();
                    if (totalRs.next()) {
                        counts.put("total", totalRs.getInt("total"));
                    }
                }

                // Get counts by status
                String statusSql = "SELECT a.status, COUNT(*) AS count " +
                        "FROM appointment ap " +
                        "LEFT JOIN appointment_status a ON ap.appointment_id = a.appointment_id " +
                        "WHERE ap.doctor_id = ? " +
                        "GROUP BY a.status";

                try (PreparedStatement statusStmt = conn.prepareStatement(statusSql)) {
                    statusStmt.setInt(1, doctorId);
                    ResultSet statusRs = statusStmt.executeQuery();

                    while (statusRs.next()) {
                        String status = statusRs.getString("status");
                        int count = statusRs.getInt("count");

                        if (status == null) {
                            counts.put("pending", count);
                        } else {
                            counts.put(status.toLowerCase(), count);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting appointment counts by status", e);
        }

        return counts;
    }
}