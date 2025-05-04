package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.dao.AppointmentDAO;
import com.example.hospital_management_system.model.Doctor;
import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "BookAppointmentServlet", value = "/BookAppointmentServlet")
public class BookAppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            Users user = AuthService.getCurrentUser(request);
            request.setAttribute("user", user);

            // Fetch available doctors
            List<Doctor> doctors = AppointmentDAO.getAllDoctors(); // Fetch doctors from DAO
            request.setAttribute("doctors", doctors);

            // Forward the request to the JSP page to display doctors
            request.getRequestDispatcher("/view/pagesJsp/patient/book-appointment.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is authenticated and is a patient
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            Users user = AuthService.getCurrentUser(request);
            int userId = user.getUserId();

            // Retrieve form data
            String doctorIdStr = request.getParameter("doctorId");
            String dateStr = request.getParameter("date");
            String timeSlot = request.getParameter("timeSlot");
            String reason = request.getParameter("reason");

            // Validate form data
            if (doctorIdStr == null || dateStr == null || timeSlot == null || reason == null
                    || doctorIdStr.trim().isEmpty() || dateStr.trim().isEmpty()
                    || timeSlot.trim().isEmpty() || reason.trim().isEmpty()) {
                request.setAttribute("error", "Please fill in all the fields.");
                request.getRequestDispatcher("/view/pagesJsp/patient/book-appointment.jsp").forward(request, response);
                return;
            }

            try {
                int doctorId = Integer.parseInt(doctorIdStr);
                Date appointmentDate = Date.valueOf(dateStr);

                // Call DAO to save the appointment
                AppointmentDAO appointmentDAO = new AppointmentDAO();
                boolean isBooked = appointmentDAO.bookAppointment(userId, doctorId, appointmentDate, reason, timeSlot);

                if (isBooked) {
                    request.setAttribute("message", "Appointment booked successfully!");
                } else {
                    request.setAttribute("error", "Failed to book appointment. Try again.");
                }

            } catch (IllegalArgumentException e) {
                // Catch invalid date or number format issues
                request.setAttribute("error", "Invalid input. Please check your form data.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "An unexpected error occurred. Please try again.");
            }

            // Forward back to the booking page (with either message or error)
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/pagesJsp/patient/book-appointment.jsp").forward(request, response);

        } else {
            // If not authenticated, redirect to login
            response.sendRedirect("LoginServlet");
        }
    }

}
