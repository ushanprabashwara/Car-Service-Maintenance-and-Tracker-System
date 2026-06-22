package com.carservice.entity;

public class CardPayment extends Payment {
    private String cardNumber;

    public CardPayment() { super(); }
    public CardPayment(double amount, String paymentDate, String cardNumber) {
        super(amount, paymentDate);
        this.cardNumber = cardNumber;
    }

    @Override
    public String processPayment() {
        return "Card Payment Authorized for " + cardNumber.substring(cardNumber.length() - 4) + ". Transaction successful.";
    }
}
