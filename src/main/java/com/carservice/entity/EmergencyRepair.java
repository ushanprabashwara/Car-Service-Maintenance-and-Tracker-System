package com.carservice.entity;

public class EmergencyRepair extends ServiceRecord {

    public EmergencyRepair() {
        super();
    }

    public EmergencyRepair(String recordId, String plateNumber, String description, String date, double baseCost, String ownerEmail) {
        super(recordId, plateNumber, description, date, baseCost, ownerEmail);
    }

    @Override
    public double calculateTotalCost() {
        // Emergency repairs have a 50% surcharge
        return baseCost * 1.5;
    }

    @Override
    public String getServiceType() { return "EMERGENCY"; }
}
