package com.carservice.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String role; // "USER" or "ADMIN"

    public User() {}

    public User(String email, String password, String fullName, String phoneNumber, String role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Polymorphic method to show login success message
    public String getLoginMessage() {
        return "Welcome back, " + fullName + "!";
    }

    // CSV representation for file storage
    public String toCsv() {
        return String.join(",", email, password, fullName, phoneNumber, role);
    }

    public static User fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        return new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}