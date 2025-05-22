package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.dao.PatientProfileDAO;
import com.example.hospital_management_system.dao.UserDAO;
import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PatientProfileServlet", value = "/PatientProfileServlet")
public class PatientProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(PatientProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);

            // Get patient medical information
            PatientProfileDAO patientDAO = new PatientProfileDAO();
            Map<String, String> medicalInfo = patientDAO.getPatientMedicalInfo(user.getUserId());

            // Set attributes for the JSP
            request.setAttribute("patient", user);
            request.setAttribute("medicalInfo", medicalInfo);

            // Log what we're sending to the JSP
            LOGGER.info("Patient: " + user.getName() + ", Medical info map size: " + medicalInfo.size());

            request.getRequestDispatcher("/view/pagesJsp/patient/patientProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (AuthService.isAuthenticated(request) && AuthService.isPatient(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);

            try {
                // Get form data
                String name = request.getParameter("name");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String gender = request.getParameter("gender");
                String dateOfBirth = request.getParameter("dateOfBirth");
                String bloodGroup = request.getParameter("bloodGroup");
                String emergencyContact = request.getParameter("emergencyContact");
                String allergies = request.getParameter("allergies");
                String medicalConditions = request.getParameter("medicalConditions");

                // Validate input
                StringBuilder errorMessage = new StringBuilder();

                if (name == null || name.trim().isEmpty()) {
                    errorMessage.append("Name is required. ");
                } else if (name.length() < 2 || name.length() > 50) {
                    errorMessage.append("Name must be between 2 and 50 characters. ");
                }

                if (phone != null && !phone.trim().isEmpty()) {
                    if (!phone.matches("\\d{10,15}")) {
                        errorMessage.append("Phone number must be 10-15 digits. ");
                    }
                }

                // Update user object with new values
                user.setName(name);
                user.setPhone(phone);
                user.setAddress(address);
                user.setGender(gender);

                Map<String, String> medicalInfo = new java.util.HashMap<>();
                medicalInfo.put("dateOfBirth", dateOfBirth);
                medicalInfo.put("bloodGroup", bloodGroup);
                medicalInfo.put("emergencyContact", emergencyContact);
                medicalInfo.put("allergies", allergies);
                medicalInfo.put("medicalConditions", medicalConditions);

                // If there are validation errors, return to the form with error messages
                if (errorMessage.length() > 0) {
                    request.setAttribute("errorMessage", errorMessage.toString());
                    request.setAttribute("patient", user);
                    request.setAttribute("medicalInfo", medicalInfo);
                    request.getRequestDispatcher("/view/pagesJsp/patient/patientProfile.jsp").forward(request, response);
                    return;
                }

                // Update the database
                PatientProfileDAO patientDAO = new PatientProfileDAO();
                boolean basicInfoUpdated = patientDAO.updatePatientBasicInfo(user);

                LOGGER.info("Basic info update result: " + basicInfoUpdated);

                boolean medicalInfoUpdated = true;

                try {
                    medicalInfoUpdated = patientDAO.updatePatientMedicalInfo(
                            user.getUserId(), dateOfBirth, bloodGroup, emergencyContact, allergies, medicalConditions);
                    LOGGER.info("Medical info update result: " + medicalInfoUpdated);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error updating medical info, table might not exist: " + e.getMessage());
                    // If the table doesn't exist, we'll just consider the basic info update as success
                    medicalInfoUpdated = true;
                }

                if (basicInfoUpdated) {
                    request.setAttribute("successMessage", "Profile updated successfully.");

                    // Update the session with the new user information
                    HttpSession session = request.getSession();
                    session.setAttribute("currentUser", user);
                } else {
                    request.setAttribute("errorMessage", "Failed to update profile. Please try again.");
                }

                // Get updated medical information for display
                try {
                    Map<String, String> updatedMedicalInfo = patientDAO.getPatientMedicalInfo(user.getUserId());
                    request.setAttribute("medicalInfo", updatedMedicalInfo);
                } catch (Exception e) {

                    request.setAttribute("medicalInfo", medicalInfo);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating patient profile", e);
                request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");

                Map<String, String> emptyMedicalInfo = new java.util.HashMap<>();
                request.setAttribute("medicalInfo", emptyMedicalInfo);
            }

            // Set the patient attribute for the JSP
            request.setAttribute("patient", user);

            request.getRequestDispatcher("/view/pagesJsp/patient/patientProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect("LoginServlet");
        }
    }
}