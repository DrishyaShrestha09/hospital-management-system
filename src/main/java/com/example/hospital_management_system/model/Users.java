package com.example.hospital_management_system.model;

import com.example.hospital_management_system.utils.PasswordHashUtils;

public class Users {

    public enum Role {
        ADMIN,
        DOCTOR,
        PATIENT
    }

    private int userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private Role role;
    private byte[] profile;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // If the password is already hashed (starts with $2a$), store it as is
        if (password != null && password.startsWith("$2a$")) {
            this.password = password;
        } else if (password != null && !password.isEmpty()) {
            // Otherwise, hash the password before storing
            this.password = PasswordHashUtils.hashPassword(password);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

    /**
     * Verifies the given plain password against the hashed password.
     *
     * @param inputPassword the plain password entered by user
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String inputPassword) {
        if (inputPassword == null || this.password == null) {
            return false;
        }
        return PasswordHashUtils.verifyPassword(inputPassword, this.password);
    }
}
