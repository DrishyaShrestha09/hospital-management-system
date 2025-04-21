package com.example.hospital_management_system.controller;

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
        // Forward the request to the login page if GET method is used
        request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection conn = DBConnectionUtils.getConnection()) {

            // SQL query to fetch user details by email
            String sql = "SELECT * FROM users WHERE user_email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Check if user exists in the database
            if (rs.next()) {
                String storedHash = rs.getString("user_password");

                // Verify the password using PasswordHashUtils
                if (PasswordHashUtils.verifyPassword(password, storedHash)) {
                    // Create a session for the logged-in user
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", rs.getInt("user_id"));
                    session.setAttribute("name", rs.getString("user_name"));
                    session.setAttribute("role", rs.getString("role"));

                    // Set success message in the session
                    session.setAttribute("login_message", "Login successful!");

                    // Redirect based on user role
                    String role = rs.getString("role");
                    switch (role) {
                        case "admin":
                            response.sendRedirect(request.getContextPath() + "/index.jsp");
                            break;
                        case "doctor":
                            response.sendRedirect(request.getContextPath() + "/view/pagesJsp/doctor/doctorDashboard.jsp");
                            break;
                        case "patient":
                            response.sendRedirect(request.getContextPath() + "/view/pagesJsp/patient/patientDashboard.jsp");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/user/dashboard.jsp");
                            break;
                    }
                } else {
                    // If password is incorrect, show an error message
                    request.setAttribute("passwordError", "Invalid password.");
                    request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
                }
            } else {
                // If user is not found in the database, show an error message
                request.setAttribute("emailError", "Invalid email");
                request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            // If there is a server error, show the error message
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
        }
    }
}
