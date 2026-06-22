package com.carservice.entity;

import java.io.Serializable;

public abstract class Payment implements Serializable {
    private double amount;
    private String paymentDate;

    public Payment() {}
    public Payment(double amount, String paymentDate) {
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public abstract String processPayment();
}
