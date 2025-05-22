package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.services.AdminService;
import com.example.hospital_management_system.services.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "DeleteDoctorServlet", value = "/DeleteDoctorServlet")
public class DeleteDoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DeleteDoctorServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        if (!AuthService.isAdmin(request)) {
            LOGGER.warning("Unauthorized access attempt to delete doctor");
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get doctor ID from request
        String doctorIdParam = request.getParameter("doctorId");
        LOGGER.info("Received delete request for doctor ID: " + doctorIdParam);

        if (doctorIdParam == null || doctorIdParam.isEmpty()) {
            LOGGER.warning("Doctor ID parameter is missing");
            request.getSession().setAttribute("errorMessage", "Doctor ID is required");
            response.sendRedirect(request.getContextPath() + "/AdminServlet");
            return;
        }

        try {
            int doctorId = Integer.parseInt(doctorIdParam);
            LOGGER.info("Attempting to delete doctor with ID: " + doctorId);

            boolean deleted = AdminService.deleteDoctor(doctorId);

            if (deleted) {
                LOGGER.info("Doctor deleted successfully: " + doctorId);
                request.getSession().setAttribute("successMessage", "Doctor completely removed from the system");
            } else {
                LOGGER.warning("Failed to delete doctor: " + doctorId);
                request.getSession().setAttribute("errorMessage", "Failed to delete doctor. Check server logs for details.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid doctor ID format: " + doctorIdParam, e);
            request.getSession().setAttribute("errorMessage", "Invalid doctor ID format");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting doctor: " + e.getMessage(), e);
            request.getSession().setAttribute("errorMessage", "Error deleting doctor: " + e.getMessage());
        }

        // Redirect back to admin dashboard
        response.sendRedirect(request.getContextPath() + "/AdminServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
