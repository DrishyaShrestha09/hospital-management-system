package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AppointmentService;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/CancelAppointmentServlet")
public class CancelAppointmentServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CancelAppointmentServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is authenticated and is a patient
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            try {
                // Get appointment ID from request parameter
                int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

                // Get current user
                Users user = AuthService.getCurrentUser(request);

                // Cancel the appointment
                boolean success = AppointmentService.getInstance().cancelAppointment(appointmentId, user.getUserId());

                // Set response type to JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                if (success) {
                    // Set a session attribute with the success message
                    HttpSession session = request.getSession();
                    session.setAttribute("cancelMessage", "Your appointment has been successfully cancelled.");

                    // Return success response
                    out.print("{\"success\":true,\"message\":\"Appointment cancelled successfully\"}");
                } else {
                    // Return error response
                    out.print("{\"success\":false,\"message\":\"Failed to cancel appointment. You may only cancel your own appointments.\"}");
                }
                out.flush();

            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid appointment ID provided", e);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"message\":\"Invalid appointment ID\"}");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error cancelling appointment", e);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"message\":\"An unexpected error occurred: " + e.getMessage() + "\"}");
            }
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized access\"}");
        }
    }
}
