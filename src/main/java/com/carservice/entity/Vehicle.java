package com.carservice.entity;
import java.io.Serializable;

public abstract class Vehicle implements Serializable {
    private String plateNumber;
    private String model;
    private int year;
    private String ownerEmail;
    private String vehicleType; // "CAR" or "MOTORBIKE"
    public Vehicle() {}
    public Vehicle(String plateNumber, String model, int year, String ownerEmail, String vehicleType) {
        this.plateNumber = plateNumber;
        this.model = model;
        this.year = year;
        this.ownerEmail = ownerEmail;
        this.vehicleType = vehicleType;
    }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    // Polymorphic display method
    public abstract String getVehicleSummary();
    public String toCsv() {
        return String.join(",", plateNumber, model, String.valueOf(year), ownerEmail, vehicleType);
    }
}
