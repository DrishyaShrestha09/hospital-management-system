package com.example.hospital_management_system.dao;
import com.example.hospital_management_system.model.Doctor;
import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Get all available doctors
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
}
