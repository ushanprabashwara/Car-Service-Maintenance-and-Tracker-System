package com.carservice.entity;

public class AdminUser extends User {
    private String adminPrivileges;

    public AdminUser() {
        super();
    }

    public AdminUser(String email, String password, String fullName, String phoneNumber, String adminPrivileges) {
        super(email, password, fullName, phoneNumber, "ADMIN");
        this.adminPrivileges = adminPrivileges;
    }

    public String getAdminPrivileges() { return adminPrivileges; }
    public void setAdminPrivileges(String adminPrivileges) { this.adminPrivileges = adminPrivileges; }

    @Override
    public String getLoginMessage() {
        return "Hello Administrator, " + getFullName() + ". System diagnostics are ready.";
    }

    @Override
    public String toCsv() {
        return super.toCsv() + "," + adminPrivileges;
    }

    public static AdminUser fromCsvWithAdmin(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 6) return null;
        return new AdminUser(parts[0], parts[1], parts[2], parts[3], parts[5]);
    }
}