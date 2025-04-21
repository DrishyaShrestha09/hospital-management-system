package com.example.hospital_management_system.model;

public class DoctorUser {
    private int doctorId;
    private int userId;

    public DoctorUser() {}

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
