package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AppointmentService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DocHome")
public class DocHomeServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DocHomeServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            Users user = AuthService.getCurrentUser(request);

            // Null check for user before proceeding
            if (user != null) {
                request.setAttribute("currentUser", user);

                // Fetch appointments for the doctor
                List<Map<String, String>> appointments = AppointmentService.getInstance().getAppointmentsForDoctor(user.getUserId());

                if (appointments == null || appointments.isEmpty()) {
                    LOGGER.info("No appointments found for doctor with ID: " + user.getUserId());
                    // Set default values for statistics
                    request.setAttribute("todayCount", 0);
                    request.setAttribute("pendingCount", 0);
                    request.setAttribute("uniquePatientCount", 0);
                    request.setAttribute("completedCount", 0);
                } else {
                    LOGGER.info("Found " + appointments.size() + " appointments for doctor with ID: " + user.getUserId());

                    // Calculate statistics
                    calculateAndSetStatistics(request, appointments);
                }

                // Set the appointments list to request attributes
                request.setAttribute("appointments", appointments);

                // Forward to the doctor dashboard
                request.getRequestDispatcher("/view/pagesJsp/doctor/doctorDashboard.jsp").forward(request, response);
            } else {
                // Handle the case where user is null
                LOGGER.warning("User is null in DocHomeServlet");
                response.sendRedirect("LoginServlet");
            }
        } else {
            LOGGER.warning("Unauthorized access attempt to DocHomeServlet");
            response.sendRedirect("LoginServlet");
        }
    }

    /**
     * Calculate statistics from appointments and set them as request attributes
     */
    private void calculateAndSetStatistics(HttpServletRequest request, List<Map<String, String>> appointments) {
        try {
            int todayCount = 0;
            int pendingCount = 0;
            int completedCount = 0;
            Set<String> uniquePatients = new HashSet<>();

            // Get today's date in the format used in appointments
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayStr = today.format(formatter);

            LOGGER.info("Calculating statistics for today: " + todayStr);

            for (Map<String, String> appointment : appointments) {
                // Count today's appointments
                if (appointment.get("date") != null && appointment.get("date").startsWith(todayStr)) {
                    todayCount++;
                }

                // Count by status
                String status = appointment.get("status");
                if ("pending".equals(status)) {
                    pendingCount++;
                } else if ("completed".equals(status)) {
                    completedCount++;
                }

                // Count unique patients
                if (appointment.get("patientName") != null) {
                    uniquePatients.add(appointment.get("patientName"));
                }
            }

            // Set the statistics as request attributes
            request.setAttribute("todayCount", todayCount);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("uniquePatientCount", uniquePatients.size());
            request.setAttribute("completedCount", completedCount);

            LOGGER.info("Statistics calculated - Today: " + todayCount +
                    ", Pending: " + pendingCount +
                    ", Unique Patients: " + uniquePatients.size() +
                    ", Completed: " + completedCount);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating statistics", e);
            // Set default values in case of error
            request.setAttribute("todayCount", 0);
            request.setAttribute("pendingCount", 0);
            request.setAttribute("uniquePatientCount", 0);
            request.setAttribute("completedCount", 0);
        }
    }
}
