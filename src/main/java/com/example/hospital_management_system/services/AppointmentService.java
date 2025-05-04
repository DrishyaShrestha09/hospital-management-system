package com.example.hospital_management_system.services;

import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class AppointmentService {
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
    public List<Map<String, String>> getAppointmentsForPatient(int patientId) {
        List<Map<String, String>> appointments = new ArrayList<>();

        String sql = "SELECT a.appointment_id, u.user_name AS doctorName, a.appointment_date, a.time_slot, a.cause " +
                "FROM appointment a " +
                "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "WHERE a.patient_id = ? " +
                "ORDER BY a.appointment_date DESC";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> appointment = new HashMap<>();
                appointment.put("appointmentId", String.valueOf(rs.getInt("appointment_id")));
                appointment.put("doctorName", rs.getString("doctorName"));
                appointment.put("appointmentDate", rs.getString("appointment_date"));
                appointment.put("timeSlot", rs.getString("time_slot"));
                appointment.put("cause", rs.getString("cause"));
                appointments.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    /**
     * Get appointments for a doctor based on userId from session
     */
    public List<Map<String, String>> getAppointmentsForDoctor(int userId) {
        System.out.println("Called getAppointmentsForDoctor with userId: " + userId);

        List<Map<String, String>> appointments = new ArrayList<>();

        String getDoctorIdSql = "SELECT doctor_id FROM doctor WHERE user_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement getDoctorIdStmt = conn.prepareStatement(getDoctorIdSql)) {

            getDoctorIdStmt.setInt(1, userId);
            ResultSet doctorRs = getDoctorIdStmt.executeQuery();

            if (doctorRs.next()) {
                int doctorId = doctorRs.getInt("doctor_id");
                System.out.println("Found doctorId: " + doctorId + " for userId: " + userId);

                // Fetch appointments using doctorId
                String appointmentSql = "SELECT a.appointment_id, u.user_name AS patientName, a.appointment_date, a.time_slot, a.cause " +
                        "FROM appointment a " +
                        "JOIN patient p ON a.patient_id = p.patient_id " +
                        "JOIN users u ON p.user_id = u.user_id " +
                        "WHERE a.doctor_id = ? " +
                        "ORDER BY a.appointment_date DESC";

                try (PreparedStatement appointmentStmt = conn.prepareStatement(appointmentSql)) {
                    appointmentStmt.setInt(1, doctorId);
                    ResultSet appointmentRs = appointmentStmt.executeQuery();

                    // Clear the list before populating it with new data
                    appointments.clear();

                    while (appointmentRs.next()) {
                        Map<String, String> appointment = new HashMap<>();
                        appointment.put("appointmentId", String.valueOf(appointmentRs.getInt("appointment_id")));
                        appointment.put("patientName", appointmentRs.getString("patientName"));
                        appointment.put("appointmentDate", appointmentRs.getString("appointment_date"));
                        appointment.put("timeSlot", appointmentRs.getString("time_slot"));
                        appointment.put("cause", appointmentRs.getString("cause"));
                        appointments.add(appointment);
                    }

                    // Log number of appointments fetched
                    System.out.println("Total appointments fetched for doctorId " + doctorId + ": " + appointments.size());

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }
}
