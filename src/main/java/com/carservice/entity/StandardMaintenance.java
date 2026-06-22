package com.carservice.entity;
public class StandardMaintenance extends ServiceRecord {
    public StandardMaintenance() {
        super();
    }
    public StandardMaintenance(String recordId, String plateNumber, String description, String date, double baseCost, String ownerEmail) {
        super(recordId, plateNumber, description, date, baseCost, ownerEmail);
    }

    @Override
    public double calculateTotalCost() {
        return baseCost + 25.0;
    }

    @Override
    public String getServiceType() { return "STANDARD"; }
}
