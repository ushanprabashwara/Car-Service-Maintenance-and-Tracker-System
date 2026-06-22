package com.carservice.entity;

public class Car extends Vehicle {
    private int numberOfDoors;
    public Car() {
        super();
    }

    public Car(String plateNumber, String model, int year, String ownerEmail, int numberOfDoors) {
        super(plateNumber, model, year, ownerEmail, "CAR");
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors() { return numberOfDoors; }
    public void setNumberOfDoors(int numberOfDoors) { this.numberOfDoors = numberOfDoors; }

    @Override
    public String getVehicleSummary() {
        return "Car Details: [" + getModel() + "] - Plate: " + getPlateNumber() + " with " + numberOfDoors
                + " doors.";
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + numberOfDoors;
    }

    public static Car fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 6) return null;
        return new Car(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], Integer.parseInt(parts[5]));
    }
}
