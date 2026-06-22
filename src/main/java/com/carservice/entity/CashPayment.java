package com.carservice.entity;

public class CashPayment extends Payment {
    private double receivedAmount;

    public CashPayment() { super(); }
    public CashPayment(double amount, String paymentDate, double receivedAmount) {
        super(amount, paymentDate);
        this.receivedAmount = receivedAmount;
    }

    public double getReceivedAmount() { return receivedAmount; }
    public void setReceivedAmount(double receivedAmount) { this.receivedAmount = receivedAmount; }

    @Override
    public String processPayment() {
        double change = receivedAmount - getAmount();
        return "Cash Payment Processed. Change: " + String.format("%.2f", change);
    }
}
