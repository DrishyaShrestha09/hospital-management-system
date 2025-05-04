package com.example.hospital_management_system.services;

import com.example.hospital_management_system.dao.UserDAO;
import com.example.hospital_management_system.model.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthService {

    public static int register(String name, String email, String password, String role, byte[] image) {
        // Create a new UserModel object
        Users user = new Users();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // Password will be hashed by UserModel.setPassword
        user.setRole(Users.Role.valueOf(role));
//        user.setImage(image);

        // Register the user and return the generated ID
        return UserDAO.registerUser(user);
    }

    public static Users login(String email, String password) {
        // Get the user by email
        Users user = UserDAO.getUserByEmail(email);

        // If user exists and password matches the hash
        if (user != null && user.verifyPassword(password)) {
            return user;
        }

        // Authentication failed
        return null;
    }

    public static Users getUserById(int id) {
        return UserDAO.getUserById(id);
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Users user = (Users) session.getAttribute("user");
        return user != null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        Users user = (Users) session.getAttribute("user");
        return user != null && user.getRole() == Users.Role.ADMIN;
    }

    public static boolean isDoctor(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        Users user = (Users) session.getAttribute("user");
        return user != null && user.getRole() == Users.Role.DOCTOR;
    }

    public static boolean isPatient(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        Users user = (Users) session.getAttribute("user");
        return user != null && user.getRole() == Users.Role.PATIENT;
    }

    public static void createUserSession(HttpServletRequest request, Users user, int timeoutSeconds) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(timeoutSeconds);
    }

    public static Users getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Users) session.getAttribute("user");
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
