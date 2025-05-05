package com.example.hospital_management_system.dao;

import com.example.hospital_management_system.utils.DBConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DepartmentDAO {
    private static final Logger LOGGER = Logger.getLogger(DepartmentDAO.class.getName());

    /**
     * Get all departments from the database
     * @return List of department maps containing department_id and department_name
     */
    public List<Map<String, Object>> getAllDepartments() {
        List<Map<String, Object>> departments = new ArrayList<>();

        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT department_id, department_name FROM department ORDER BY department_name")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> department = new HashMap<>();
                department.put("department_id", rs.getInt("department_id"));
                department.put("department_name", rs.getString("department_name"));
                departments.add(department);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving departments", e);
        }

        return departments;
    }

    /**
     * Get a department by its ID
     * @param departmentId The ID of the department to retrieve
     * @return Map containing department_id and department_name, or null if not found
     */
    public Map<String, Object> getDepartmentById(int departmentId) {
        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT department_id, department_name FROM department WHERE department_id = ?")) {

            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> department = new HashMap<>();
                department.put("department_id", rs.getInt("department_id"));
                department.put("department_name", rs.getString("department_name"));
                return department;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving department with ID: " + departmentId, e);
        }

        return null;
    }

    /**
     * Add a new department
     * @param departmentName The name of the department to add
     * @return true if the department was added successfully, false otherwise
     */
    public boolean addDepartment(String departmentName) {
        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO department (department_name) VALUES (?)")) {

            stmt.setString(1, departmentName);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding department: " + departmentName, e);
            return false;
        }
    }

    /**
     * Check if a department exists by ID
     * @param departmentId The ID of the department to check
     * @return true if the department exists, false otherwise
     */
    public boolean departmentExists(int departmentId) {
        try (Connection conn = DBConnectionUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM department WHERE department_id = ?")) {

            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if department exists with ID: " + departmentId, e);
            return false;
        }
    }
}