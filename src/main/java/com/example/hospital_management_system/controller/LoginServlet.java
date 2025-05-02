package com.example.hospital_management_system.controller;

import com.example.hospital_management_system.model.Users;
import com.example.hospital_management_system.services.AuthService;
import com.example.hospital_management_system.utils.DBConnectionUtils;
import com.example.hospital_management_system.utils.PasswordHashUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login page on GET request
        request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnectionUtils.getConnection()) {

            String sql = "SELECT * FROM users WHERE user_email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("user_password");

                if (PasswordHashUtils.verifyPassword(password, storedHash)) {
                    // Create a new Users object
                    Users user = new Users();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("user_name"));
                    user.setEmail(rs.getString("user_email"));

                    // Ensure the Role column in the database is named correctly
                    user.setRole(Users.Role.valueOf(rs.getString("role").toUpperCase()));  // Adjusted column name and case

                    // Set session via AuthService
                    AuthService.createUserSession(request, user, 3600);  // 1 hour session

                    // Redirect based on role
                    switch (user.getRole()) {
                        case ADMIN:
                            response.sendRedirect(request.getContextPath() + "/index.jsp?login=success");
                            break;
                        case DOCTOR:
                            response.sendRedirect(request.getContextPath() + "/DocHome");
                            break;
                        case PATIENT:
                            response.sendRedirect(request.getContextPath() + "/PatientDashboardServlet");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/user/dashboard.jsp?login=success");
                            break;
                    }
                } else {
                    request.setAttribute("passwordError", "Invalid password.");
                    request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("emailError", "Invalid email.");
                request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
        }
    }
}
