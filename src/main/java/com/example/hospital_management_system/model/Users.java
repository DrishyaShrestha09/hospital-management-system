package com.example.hospital_management_system.model;
import org.mindrot.jbcrypt.BCrypt;
public class Users {

    public enum Role {admin, doctor, patient}

    private int userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String gender;
    private Role role;
    private byte[] profile;

    public Users() {}

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
        // Check if the password is already hashed (starts with $2a$)
        if (password != null && !password.startsWith("$2a$")) {
            // Hash the password with BCrypt
            this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        } else {
            // Password is already hashed or null
            this.password = password;
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

    public boolean verifyPassword(String plainTextPassword) {
        if (this.password == null || plainTextPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, this.password);
    }

}
