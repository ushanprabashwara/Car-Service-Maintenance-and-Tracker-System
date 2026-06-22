package com.carservice.entity;

public class DashboardReminder extends Reminder {
    
    public DashboardReminder() {
        super();
    }

    public DashboardReminder(String plateNumber, String appointmentDate, String serviceType, String ownerEmail) {
        super(plateNumber, appointmentDate, serviceType, ownerEmail, "DASHBOARD");
    }

    @Override
    public String getNotificationText() {
        return "Dashboard Alert: Vehicle " + getPlateNumber() + " scheduled for " + getServiceType() + " on " + getAppointmentDate();
    }

    public static DashboardReminder fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        return new DashboardReminder(parts[0], parts[1], parts[2], parts[3]);
    }
}
