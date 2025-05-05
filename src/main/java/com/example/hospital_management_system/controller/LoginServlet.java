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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberEmail".equals(cookie.getName())) {
                    request.setAttribute("rememberedEmail", cookie.getValue());
                    break;
                }
            }
        }
        request.getRequestDispatcher("/view/pagesJsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        try (Connection conn = DBConnectionUtils.getConnection()) {

            String sql = "SELECT * FROM users WHERE user_email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("user_password");

                if (PasswordHashUtils.verifyPassword(password, storedHash)) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("user_id"));
                    user.setName(rs.getString("user_name"));
                    user.setEmail(rs.getString("user_email"));

                    user.setRole(Users.Role.valueOf(rs.getString("role").toUpperCase()));

                    AuthService.createUserSession(request, user, 3600);

                    if ("true".equals(remember)) {
                        Cookie rememberCookie = new Cookie("rememberEmail", email);
                        rememberCookie.setMaxAge(7 * 24 * 60 * 60);
                        rememberCookie.setPath(request.getContextPath());
                        response.addCookie(rememberCookie);
                    } else {
                        Cookie[] cookies = request.getCookies();
                        if (cookies != null) {
                            for (Cookie cookie : cookies) {
                                if ("rememberEmail".equals(cookie.getName())) {
                                    cookie.setMaxAge(0);
                                    cookie.setPath(request.getContextPath());
                                    response.addCookie(cookie);
                                }
                            }
                        }
                    }

                    // Redirect user based on role
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
                    request.setAttribute("email", email);
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
