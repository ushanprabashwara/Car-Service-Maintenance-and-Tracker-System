package com.carservice.entity;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Reminder implements Serializable {
    private String plateNumber;
    private String appointmentDate; // YYYY-MM-DD
    private String serviceType;
    private String ownerEmail;
    private String reminderType; // "EMAIL" or "DASHBOARD"

    public Reminder() {}

    public Reminder(String plateNumber, String appointmentDate, String serviceType, String ownerEmail, String reminderType) {
        this.plateNumber = plateNumber;
        this.appointmentDate = appointmentDate;
        this.serviceType = serviceType;
        this.ownerEmail = ownerEmail;
        this.reminderType = reminderType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getReminderType() {
        return reminderType;
    }
    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public boolean isOverdue() {
        try {
            LocalDate appointment = LocalDate.parse(appointmentDate);
            return LocalDate.now().isAfter(appointment);
        } catch (Exception e) {
            return false;
        }
    }

    public abstract String getNotificationText();

    public String toCsv() {
        return String.join(",", plateNumber, appointmentDate, serviceType, ownerEmail, reminderType);
    }
}
