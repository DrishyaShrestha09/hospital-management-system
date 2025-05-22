package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.services.AdminService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "EditDoctorServlet", value = "/EditDoctorServlet")
public class EditDoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EditDoctorServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthService.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        // Get parameters from the form
        String doctorIdStr = request.getParameter("doctorId");
        String name = request.getParameter("name");
        String specialty = request.getParameter("specialty");
        String email = request.getParameter("email");

        LOGGER.info("Received edit request for doctor ID: " + doctorIdStr);
        LOGGER.info("Name: " + name + ", Specialty: " + specialty + ", Email: " + email);

        // Validate input
        if (doctorIdStr == null || doctorIdStr.isEmpty() ||
                name == null || name.isEmpty() ||
                specialty == null || specialty.isEmpty() ||
                email == null || email.isEmpty()) {

            // Set error message and redirect back to admin page
            request.getSession().setAttribute("errorMessage", "All fields are required");
            response.sendRedirect(request.getContextPath() + "/AdminServlet");
            return;
        }

        try {
            int doctorId = Integer.parseInt(doctorIdStr);

            boolean success = AdminService.updateDoctor(doctorId, name, specialty, email);

            if (success) {
                request.getSession().setAttribute("successMessage", "Doctor updated successfully");
                LOGGER.info("Doctor updated successfully: " + doctorId);
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update doctor");
                LOGGER.warning("Failed to update doctor: " + doctorId);
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid doctor ID");
            LOGGER.log(Level.WARNING, "Invalid doctor ID: " + doctorIdStr, e);
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error updating doctor", e);
        }

        response.sendRedirect(request.getContextPath() + "/AdminServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/AdminServlet");
    }
}