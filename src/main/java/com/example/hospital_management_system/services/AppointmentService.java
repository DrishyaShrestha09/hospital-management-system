package com.example.hospital_management_system.services;

import com.example.hospital_management_system.model.Appointment;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AppointmentService {
    private static AppointmentService instance;

    private AppointmentService() {}

    public static AppointmentService getInstance() {
        if (instance == null) {
            instance = new AppointmentService();
        }
        return instance;
    }

    public List<Map<String, String>> getAppointmentsForPatient(int patientId) {
        List<Map<String, String>> appointments = new ArrayList<>();
        // Replace this mock with actual database fetch logic.
        // Example data structure for appointments
        appointments.add(Map.of("id", "1", "doctorName", "Dr. Smith", "date", "2025-05-10", "timeSlot", "10:00 AM", "status", "pending"));
        appointments.add(Map.of("id", "2", "doctorName", "Dr. Johnson", "date", "2025-05-12", "timeSlot", "2:00 PM", "status", "completed"));
        return appointments;
    }

    public List<Map<String, String>> getAppointmentsForDoctor(int doctorId) {
        List<Map<String, String>> appointments = new ArrayList<>();
        // Replace this mock with actual database fetch logic.
        appointments.add(Map.of("id", "1", "patientName", "John Doe", "date", "2025-05-10", "timeSlot", "10:00 AM", "status", "pending"));
        appointments.add(Map.of("id", "2", "patientName", "Jane Doe", "date", "2025-05-12", "timeSlot", "2:00 PM", "status", "completed"));
        return appointments;
    }
}
