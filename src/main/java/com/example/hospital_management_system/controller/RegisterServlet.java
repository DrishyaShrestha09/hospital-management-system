package com.example.hospital_management_system.controller;

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
        String phone = "";
        String address = "";
        String gender = "";

        // Set user object
        Users user = new Users();
        user.setName(firstName + " " + lastName);
        user.setEmail(email);
        String hashedPassword = PasswordHashUtils.hashPassword(password);
        user.setPassword(hashedPassword);
        user.setPhone(phone);
        user.setAddress(address);
        user.setGender(gender);
        user.setProfile(null);

        try {
            Users.Role role = Users.Role.valueOf(userType.toLowerCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid user role selected.");
            request.getRequestDispatcher("/view/pagesJsp/signup.jsp").forward(request, response);
            return;
        }

        // Register user via DAO
        int userId = UserDAO.registerUser(user);

        if (userId > 0) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
        } else {
            request.setAttribute("errorMessage", "Registration failed. Try again.");
            request.getRequestDispatcher("/view/pagesJsp/signup.jsp").forward(request, response);

        }
    }
}
