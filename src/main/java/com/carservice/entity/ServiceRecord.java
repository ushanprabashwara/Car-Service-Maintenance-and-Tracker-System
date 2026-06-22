package com.carservice.entity;

import java.io.Serializable;

public abstract class ServiceRecord implements Serializable {

    private String recordId;
    private String plateNumber;
    private String description;
    private String date;
    protected double baseCost;
    private String ownerEmail;

    public ServiceRecord() {
    }

    public ServiceRecord(String recordId, String plateNumber, String description, String date, double baseCost, String ownerEmail) {

        this.recordId = recordId;
        this.plateNumber = plateNumber;
        this.description = description;
        this.date = date;
        this.baseCost = baseCost;
        this.ownerEmail = ownerEmail;
    }

    public abstract double calculateTotalCost();

    public double getTotalCost() {
        return calculateTotalCost();
    }

    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(double baseCost) {
        this.baseCost = baseCost;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String toCsv() {
        return String.join(",", recordId, plateNumber, description, date,
                String.valueOf(baseCost), ownerEmail, getServiceType());
    }

    public abstract String getServiceType();
}