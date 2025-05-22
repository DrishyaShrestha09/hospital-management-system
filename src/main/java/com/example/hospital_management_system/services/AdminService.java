package com.example.hospital_management_system.services;

import com.example.hospital_management_system.dao.AdminDAO;
import com.example.hospital_management_system.model.Users;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AdminService {
    private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());
    private static AdminDAO adminDAO = new AdminDAO();

    public static List<Map<String, Object>> getPatientsWithAppointmentCount() {
        return adminDAO.getPatientsWithAppointments();
    }

    public static List<Map<String, Object>> getAllDoctors() {
        return adminDAO.getAllDoctors();
    }

    public static boolean deleteDoctor(int doctorId) {
        LOGGER.info("AdminService: Attempting to delete doctor with ID: " + doctorId);
        boolean result = adminDAO.deleteDoctor(doctorId);
        LOGGER.info("AdminService: Delete doctor result: " + result);
        return result;
    }


    public static boolean updateDoctor(int doctorId, String name, String specialty, String email) {
        LOGGER.info("AdminService: Attempting to update doctor with ID: " + doctorId);
        boolean result = adminDAO.updateDoctor(doctorId, name, specialty, email);
        LOGGER.info("AdminService: Update doctor result: " + result);
        return result;
    }
}
