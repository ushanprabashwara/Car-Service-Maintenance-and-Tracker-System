package com.carservice.entity;

import java.io.Serializable;

public class Invoice implements Serializable {
    private String invoiceNumber;
    private String plateNumber;
    private double amount;
    private String status; // "PAID", "PENDING"
    private String ownerEmail;
    private String date;

    public Invoice() {}
    public Invoice(String invoiceNumber, String plateNumber, double amount, String status, String ownerEmail, String date) {
        this.invoiceNumber = invoiceNumber;
        this.plateNumber = plateNumber;
        this.amount = amount;
        this.status = status;
        this.ownerEmail = ownerEmail;
        this.date = date;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String toCsv() {
        return String.join(",", invoiceNumber, plateNumber, String.valueOf(amount), status, ownerEmail, date);
    }

    public static Invoice fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 6) return null;
        return new Invoice(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3], parts[4], parts[5]);
    }
}
