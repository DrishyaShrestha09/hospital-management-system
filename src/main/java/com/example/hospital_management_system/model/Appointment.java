package com.example.hospital_management_system.model;

import java.time.LocalDate;

public class Appointment {
    private int appointmentId;
    private LocalDate appointmentDate;
    private String cause;
    private String timeSlot;
    private String patientName;
    private int doctorId;

    // Default Constructor
    public Appointment() {}

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentDate=" + appointmentDate +
                ", cause='" + cause + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", patientName='" + patientName + '\'' +
                ", doctorId=" + doctorId +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment that = (Appointment) obj;
        return appointmentId == that.appointmentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(appointmentId);
    }
}
