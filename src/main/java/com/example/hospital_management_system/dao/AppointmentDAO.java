package com.example.hospital_management_system.dao;
import com.example.hospital_management_system.model.Appointment;
import com.example.hospital_management_system.model.Doctor;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AppointmentDAO {

    // Get all available doctors with their names from the users table
    public static List<Map<String, Object>> getAllDoctorsWithNames() {
        List<Map<String, Object>> doctors = new ArrayList<>();
        String sql = "SELECT d.doctor_id, d.specialty, d.experience, d.department_id, u.user_name " +
                "FROM doctor d " +
                "JOIN users u ON d.user_id = u.user_id";

        try (Connection conn = DBConnectionUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> doctor = new HashMap<>();
                doctor.put("doctorId", rs.getInt("doctor_id"));
                doctor.put("specialty", rs.getString("specialty"));
                doctor.put("experience", rs.getInt("experience"));
                doctor.put("departmentId", rs.getInt("department_id"));
                doctor.put("name", rs.getString("user_name"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Original getAllDoctors method - keep for backward compatibility
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";

        try (Connection conn = DBConnectionUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setSpecialty(rs.getString("specialty"));
                doctor.setExperience(rs.getInt("experience"));
                doctor.setDepartmentId(rs.getInt("department_id"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Book an appointment
    public boolean bookAppointment(int userId, int doctorId, Date date, String cause, String timeSlot) {
        String getPatientIdSQL = "SELECT patient_id FROM patient WHERE user_id = ?";
        String insertAppointmentSQL = "INSERT INTO appointment (appointment_date, cause, doctor_id, patient_id, time_slot) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(getPatientIdSQL);
             PreparedStatement ps2 = conn.prepareStatement(insertAppointmentSQL)) {

            // Get patient_id from user_id
            ps1.setInt(1, userId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                int patientId = rs.getInt("patient_id");

                ps2.setDate(1, date);
                ps2.setString(2, cause);
                ps2.setInt(3, doctorId);
                ps2.setInt(4, patientId);
                ps2.setString(5, timeSlot);

                int rows = ps2.executeUpdate();
                return rows > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Appointment> getAppointmentsByUserId(int userId) {
        List<Appointment> appointments = new ArrayList<>();
        String getDoctorIdSQL = "SELECT d.doctor_id FROM doctor d " +
                "JOIN user u ON d.user_id = u.user_id " +
                "WHERE u.user_id = ?";

        String getAppointmentsSQL = "SELECT a.appointment_id, a.appointment_date, a.time_slot, a.cause " +
                "FROM appointment a " +
                "WHERE a.doctor_id = ?";

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(getDoctorIdSQL);
             PreparedStatement stmt2 = conn.prepareStatement(getAppointmentsSQL)) {

            // First, get the doctor_id using user_id
            stmt1.setInt(1, userId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    int doctorId = rs1.getInt("doctor_id");

                    // Now, get the appointments for that doctor_id
                    stmt2.setInt(1, doctorId);
                    try (ResultSet rs2 = stmt2.executeQuery()) {
                        while (rs2.next()) {
                            Appointment appt = new Appointment();
                            appt.setAppointmentId(rs2.getInt("appointment_id"));
                            appt.setAppointmentDate(rs2.getDate("appointment_date").toLocalDate());
                            appt.setTimeSlot(rs2.getString("time_slot"));
                            appt.setCause(rs2.getString("cause"));
                            appointments.add(appt);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Consider logging this exception instead of printing
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this exception instead of printing
        }

        return appointments;
    }
}
