package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.dao.DepartmentDAO;
import com.example.hospital_management_system.dao.DoctorDAO;
import com.example.hospital_management_system.dao.UserDAO;
import com.example.hospital_management_system.model.Doctor;
import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService; // Updated import path
import com.example.hospital_management_system.utils.PasswordHashUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/DoctorProfile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 5 * 1024 * 1024,   // 5 MB
        maxRequestSize = 10 * 1024 * 1024 // 10 MB
)
public class DoctorProfileServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DoctorProfileServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if the user is authenticated and is a doctor
        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            // Get the user from the session
            Users user = AuthService.getCurrentUser(request);
            LOGGER.info("Doctor profile accessed by user ID: " + user.getUserId());

            // Get doctor information
            DoctorDAO doctorDAO = new DoctorDAO();
            Doctor doctor = doctorDAO.getDoctorByUserId(user.getUserId());

            if (doctor == null) {
                LOGGER.warning("No doctor profile found for user ID: " + user.getUserId());
                request.setAttribute("errorMessage", "Doctor profile not found. Please contact administrator.");
            } else {
                LOGGER.info("Retrieved doctor profile with ID: " + doctor.getDoctorId());
            }

            // Get departments for dropdown
            DepartmentDAO departmentDAO = new DepartmentDAO();
            List<Map<String, Object>> departments = departmentDAO.getAllDepartments();

            // Set attributes and forward to doctor's profile page
            request.setAttribute("user", user);
            request.setAttribute("doctor", doctor);
            request.setAttribute("departments", departments);
            request.getRequestDispatcher("/view/pagesJsp/doctor/doctorProfile.jsp").forward(request, response);
        } else {
            // Redirect unauthenticated users or non-doctors to login
            LOGGER.warning("Unauthorized access attempt to doctor profile");
            response.sendRedirect("LoginServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (AuthService.isAuthenticated(request) && AuthService.isDoctor(request)) {
            // Get the user from the session
            Users currentUser = AuthService.getCurrentUser(request);
            request.setAttribute("user", currentUser);
            LOGGER.info("Processing doctor profile update for user ID: " + currentUser.getUserId());

            try {
                // Log all request parameters for debugging
                LOGGER.info("All request parameters:");
                request.getParameterMap().forEach((key, value) ->
                        LOGGER.info(key + ": " + String.join(", ", value))
                );

                // Create a StringBuilder for error messages
                StringBuilder errorMessage = new StringBuilder();

                // Process personal information update
                String userId = request.getParameter("userId");
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String gender = request.getParameter("gender");
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                // Process professional information update
                String doctorIdStr = request.getParameter("doctorId");
                String specialty = request.getParameter("specialty");
                String experienceStr = request.getParameter("experience");
                String departmentIdStr = request.getParameter("departmentId");

                // Log the doctor ID for debugging
                LOGGER.info("Doctor ID from form: " + doctorIdStr);

                // Validate and update user information
                boolean userUpdated = false;
                boolean passwordChanged = false;
                boolean emailChanged = !email.equals(currentUser.getEmail());

                // Create a new Users object for the update
                Users updatedUser = new Users();
                updatedUser.setUserId(currentUser.getUserId());
                updatedUser.setName(name);
                updatedUser.setEmail(email);
                updatedUser.setPhone(phone);
                updatedUser.setAddress(address);
                updatedUser.setGender(gender);
                updatedUser.setRole(currentUser.getRole());

                // Handle profile picture upload
                Part filePart = request.getPart("profilePicture");
                if (filePart != null && filePart.getSize() > 0) {
                    try (InputStream fileContent = filePart.getInputStream()) {
                        byte[] profilePicture = new byte[(int) filePart.getSize()];
                        fileContent.read(profilePicture);
                        updatedUser.setProfile(profilePicture);
                    }
                } else {
                    // Keep existing profile picture
                    updatedUser.setProfile(currentUser.getProfile());
                }

                // Check if password needs to be updated
                if (newPassword != null && !newPassword.isEmpty()) {
                    // Verify current password before allowing password change
                    if (currentPassword == null || currentPassword.isEmpty() || !currentUser.verifyPassword(currentPassword)) {
                        errorMessage.append("Current password is incorrect. ");
                    } else if (!newPassword.equals(confirmPassword)) {
                        errorMessage.append("New passwords do not match. ");
                    } else {
                        // Set the new hashed password
                        updatedUser.setPassword(PasswordHashUtils.hashPassword(newPassword));
                        passwordChanged = true;
                    }
                } else {
                    // Keep existing password
                    updatedUser.setPassword(currentUser.getPassword());
                }

                // If email is changed, verify current password
                if (emailChanged) {
                    if (currentPassword == null || currentPassword.isEmpty() || !currentUser.verifyPassword(currentPassword)) {
                        errorMessage.append("Current password is required to change email. ");
                        // Revert to original email
                        updatedUser.setEmail(currentUser.getEmail());
                    }
                }

                // Update user information if there are no errors
                if (errorMessage.length() == 0) {
                    UserDAO userDAO = new UserDAO();
                    userUpdated = userDAO.updateUser(updatedUser);

                    if (userUpdated) {
                        LOGGER.info("User information updated successfully");
                        // Update the session with the new user information
                        if (passwordChanged || emailChanged) {
                            // Force re-login if password or email changed
                            AuthService.logout(request);
                            response.sendRedirect("LoginServlet?message=Profile updated. Please login again.");
                            return;
                        } else {
                            // Update session with new user data
                            HttpSession session = request.getSession();
                            session.setAttribute("user", userDAO.getUserById(currentUser.getUserId()));
                        }
                    } else {
                        errorMessage.append("Failed to update personal information. ");
                        LOGGER.warning("Failed to update user information");
                    }
                }

                // Validate and update doctor information
                boolean doctorUpdated = false;

                // Parse and validate doctor information
                int doctorId = 0;
                try {
                    doctorId = Integer.parseInt(doctorIdStr);
                    LOGGER.info("Parsed doctor ID: " + doctorId);
                } catch (NumberFormatException e) {
                    errorMessage.append("Invalid Doctor ID format. ");
                    LOGGER.warning("Invalid doctor ID format: " + doctorIdStr);
                }

                int experience = 0;
                try {
                    experience = Integer.parseInt(experienceStr);
                    if (experience < 0) {
                        errorMessage.append("Experience cannot be negative. ");
                    }
                } catch (NumberFormatException e) {
                    errorMessage.append("Experience must be a valid number. ");
                }

                int departmentId = 0;
                try {
                    departmentId = Integer.parseInt(departmentIdStr);
                } catch (NumberFormatException e) {
                    errorMessage.append("Department ID must be a valid number. ");
                }

                // Create a Doctor object for the update
                Doctor doctor = new Doctor();
                doctor.setDoctorId(doctorId);
                doctor.setSpecialty(specialty);
                doctor.setExperience(experience);
                doctor.setDepartmentId(departmentId);

                // Update doctor information if there are no errors
                if (errorMessage.length() == 0 && doctorId > 0) {
                    DoctorDAO doctorDAO = new DoctorDAO();
                    doctorUpdated = doctorDAO.updateDoctorProfile(doctor);

                    if (doctorUpdated) {
                        LOGGER.info("Doctor information updated successfully");
                    } else {
                        errorMessage.append("Failed to update professional information. ");
                        LOGGER.warning("Failed to update doctor information");
                    }
                }

                // Set success or error message
                if (errorMessage.length() > 0) {
                    request.setAttribute("errorMessage", errorMessage.toString());
                    LOGGER.warning("Error message: " + errorMessage.toString());
                } else if (userUpdated || doctorUpdated) {
                    request.setAttribute("successMessage", "Profile updated successfully.");
                    LOGGER.info("Profile updated successfully");
                }

                // Get fresh data for the form
                Users refreshedUser = UserDAO.getUserById(currentUser.getUserId());
                Doctor refreshedDoctor = new DoctorDAO().getDoctorByUserId(currentUser.getUserId());
                List<Map<String, Object>> departments = new DepartmentDAO().getAllDepartments();

                // Set attributes for the JSP
                request.setAttribute("user", refreshedUser);
                request.setAttribute("doctor", refreshedDoctor);
                request.setAttribute("departments", departments);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating doctor profile", e);
                request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());

                // Get the current doctor profile to display in the form
                DoctorDAO dao = new DoctorDAO();
                Doctor doctor = dao.getDoctorByUserId(currentUser.getUserId());
                request.setAttribute("doctor", doctor);

                // Get departments for dropdown
                DepartmentDAO departmentDAO = new DepartmentDAO();
                List<Map<String, Object>> departments = departmentDAO.getAllDepartments();
                request.setAttribute("departments", departments);
            }

            // Forward the request to the doctor profile page
            request.getRequestDispatcher("/view/pagesJsp/doctor/doctorProfile.jsp").forward(request, response);

        } else {
            LOGGER.warning("Unauthorized post attempt to doctor profile");
            response.sendRedirect("LoginServlet");
        }
    }
}
