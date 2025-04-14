package model;

import java.time.LocalDate;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private String status;

    public Appointment() {}

    public Appointment(int id, int patientId, int doctorId, LocalDate date, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}