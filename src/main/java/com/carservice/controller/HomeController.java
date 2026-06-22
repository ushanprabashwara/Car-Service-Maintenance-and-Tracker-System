package com.carservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/vehicles/add")
    public String addVehicle() {
        return "add-vehicle";
    }

    @GetMapping("/vehicles/list")
    public String listVehicles() {
        return "vehicle-list";
    }

    @GetMapping("/services/add")
    public String addService() {
        return "add-service";
    }

    @GetMapping("/services/history")
    public String serviceHistory() {
        return "service-history";
    }

    @GetMapping("/services/edit")
    public String editService() {
        return "edit-service";
    }

    @GetMapping("/reminders/schedule")
    public String scheduleService() {
        return "schedule-service";
    }

    @GetMapping("/reminders/list")
    public String listReminders() {
        return "reminder-list";
    }

    @GetMapping("/billing/invoices")
    public String viewInvoices() {
        return "invoice-page";
    }

    @GetMapping("/billing/history")
    public String paymentHistory() {
        return "payment-history";
    }

    @GetMapping("/feedback/add")
    public String addFeedback() {
        return "add-review";
    }

    @GetMapping("/feedback/list")
    public String listReviews() {
        return "review-list";
    }

    @GetMapping("/admin/reviews")
    public String adminReviews() {
        return "admin-reviews";
    }
}
