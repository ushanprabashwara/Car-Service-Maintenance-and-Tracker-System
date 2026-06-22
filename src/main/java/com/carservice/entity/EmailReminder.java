package com.carservice.entity;

public class EmailReminder extends Reminder {
    private String email;

    public EmailReminder() {
        super();
    }

    public EmailReminder(String plateNumber, String appointmentDate, String serviceType, String ownerEmail, String email) {
        super(plateNumber, appointmentDate, serviceType, ownerEmail, "EMAIL");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getNotificationText() {
        return "Sending Email to [" + email + "]: Vehicle " + getPlateNumber() + " scheduled for " + getServiceType();
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + email;
    }

    public static EmailReminder fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 6) return null;
        return new EmailReminder(parts[0], parts[1], parts[2], parts[3], parts[5]);
    }
}