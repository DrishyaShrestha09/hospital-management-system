package com.example.hospital_management_system.model;

public class AppointmentPatient {
    private int appointmentId;
    private int patientId;

    public AppointmentPatient() {}

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
