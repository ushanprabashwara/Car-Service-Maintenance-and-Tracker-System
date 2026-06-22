package com.carservice.entity;
public class Motorbike extends Vehicle {
    private boolean hasSidecar;
    public Motorbike() {
        super();
    }
    public Motorbike(String plateNumber, String model, int year, String ownerEmail, boolean
            hasSidecar) {
        super(plateNumber, model, year, ownerEmail, "MOTORBIKE");
        this.hasSidecar = hasSidecar;
    }
    public boolean isHasSidecar() {
        return hasSidecar;
    }
    public void setHasSidecar(boolean hasSidecar) {
        this.hasSidecar = hasSidecar;
    }

    @Override
    public String getVehicleSummary() {
        return "Motorbike Details: [" + getModel() + "] - Plate: " + getPlateNumber() + (hasSidecar ? " (Sidecar Attached)" : " (Standard)");
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + hasSidecar;
    }

    public static Motorbike fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 6) return null;
        return new Motorbike(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3],
                Boolean.parseBoolean(parts[5]));
    }
}

