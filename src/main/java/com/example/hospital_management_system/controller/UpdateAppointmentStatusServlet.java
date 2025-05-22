package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.services.AppointmentService;
import com.example.hospital_management_system.services.AuthService;
import com.example.hospital_management_system.model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/updateAppointmentStatus")
public class UpdateAppointmentStatusServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateAppointmentStatusServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Check if user is authenticated and is a doctor
        if (!AuthService.isAuthenticated(request) || !AuthService.isDoctor(request)) {
            out.print("{\"success\":false,\"message\":\"Unauthorized access\"}");
            return;
        }

        try {
            // Get parameters from request
            String appointmentIdStr = request.getParameter("appointmentId");
            String status = request.getParameter("status");
            String notes = request.getParameter("notes");

            // Validate parameters
            if (appointmentIdStr == null || status == null || appointmentIdStr.isEmpty() || status.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Invalid parameters\"}");
                return;
            }

            // Parse appointment ID
            int appointmentId = Integer.parseInt(appointmentIdStr);

            // Get current user (doctor)
            Users currentUser = AuthService.getCurrentUser(request);
            int doctorUserId = currentUser.getUserId();

            boolean updated;

            // Update appointment status with or without notes
            if (notes != null && !notes.isEmpty()) {
                updated = AppointmentService.getInstance().updateAppointmentStatusWithNotes(appointmentId, status, notes, doctorUserId);
            } else {
                updated = AppointmentService.getInstance().updateAppointmentStatus(appointmentId, status, doctorUserId);
            }

            if (updated) {
                out.print("{\"success\":true,\"message\":\"Appointment status updated successfully\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Failed to update appointment status\"}");
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid appointment ID format", e);
            out.print("{\"success\":false,\"message\":\"Invalid appointment ID format\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating appointment status", e);
            out.print("{\"success\":false,\"message\":\"An error occurred while updating appointment status\"}");
        }
    }
}