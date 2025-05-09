package com.example.hospital_management_system.services;

import com.example.hospital_management_system.dao.AdminDAO;
import com.example.hospital_management_system.model.Users;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AdminService {
    private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());
    private static AdminDAO adminDAO = new AdminDAO();

    // Get a list of patients with the count of their appointments
    public static List<Map<String, Object>> getPatientsWithAppointmentCount() {
        return adminDAO.getPatientsWithAppointments();
    }

    // Get all doctors with their details
    public static List<Map<String, Object>> getAllDoctors() {
        return adminDAO.getAllDoctors();
    }

    /**
     * Delete a doctor by doctor ID
     * @param doctorId The doctor ID to delete
     * @return true if deletion was successful, false otherwise
     */
    public static boolean deleteDoctor(int doctorId) {
        LOGGER.info("AdminService: Attempting to delete doctor with ID: " + doctorId);
        boolean result = adminDAO.deleteDoctor(doctorId);
        LOGGER.info("AdminService: Delete doctor result: " + result);
        return result;
    }
}
