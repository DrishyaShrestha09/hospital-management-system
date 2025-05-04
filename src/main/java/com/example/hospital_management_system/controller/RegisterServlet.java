package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.dao.DoctorDAO;
import com.example.hospital_management_system.dao.PatientDAO;
import com.example.hospital_management_system.dao.UserDAO;
import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.utils.PasswordHashUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/pagesJsp/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Getting user input from form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        String phone = request.getParameter("phone");      // assuming these inputs exist
        String address = request.getParameter("address");  // otherwise, keep them empty
        String gender = request.getParameter("gender");

        // Set user object
        Users user = new Users();
        user.setName(firstName + " " + lastName);
        user.setEmail(email);
        String hashedPassword = PasswordHashUtils.hashPassword(password);
        user.setPassword(hashedPassword);
        user.setPhone(phone);
        user.setAddress(address);
        user.setGender(gender);
        user.setProfile(null); // no profile image on registration

        try {
            Users.Role role = Users.Role.valueOf(userType.toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid user role selected.");
            request.getRequestDispatcher("/view/pagesJsp/signup.jsp").forward(request, response);
            return;
        }

        // Register user via DAO
        int userId = UserDAO.registerUser(user);

        if (userId > 0) {
            // Based on role, insert into doctor or patient table
            if (user.getRole() == Users.Role.DOCTOR) {
                // For now default values — or retrieve from form if needed
                int experience = 0;
                String specialty = "";
             // default department for new doctor

                DoctorDAO.addDoctor(userId, experience, specialty);

            } else if (user.getRole() == Users.Role.PATIENT) {
                PatientDAO.addPatient(userId);
            }

            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        } else {
            request.setAttribute("errorMessage", "Registration failed. Try again.");
            request.getRequestDispatcher("/view/pagesJsp/signup.jsp").forward(request, response);
        }
    }
}
