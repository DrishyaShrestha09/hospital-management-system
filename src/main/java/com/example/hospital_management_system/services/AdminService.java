package com.example.hospital_management_system.services;

import com.example.hospital_management_system.dao.AdminDAO;
import com.example.hospital_management_system.model.Users;

import java.util.List;
import java.util.Map;

public class AdminService {

    private static AdminDAO adminDAO = new AdminDAO();

    // Get a list of patients with the count of their appointments
    public static List<Map<String, Object>> getPatientsWithAppointmentCount() {
        return adminDAO.getPatientsWithAppointments();
    }

    // Get all doctors with their details
    public static List<Map<String, Object>> getAllDoctors() {
        return adminDAO.getAllDoctors();
    }

    // You can add more admin-specific methods here later, like:
    // public static boolean deleteUserById(int id)
    // public static int countTotalUsers()
}
