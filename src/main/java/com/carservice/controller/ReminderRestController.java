package com.carservice.controller;

import com.carservice.entity.DashboardReminder;
import com.carservice.entity.EmailReminder;
import com.carservice.entity.Reminder;
import com.carservice.entity.User;
import com.carservice.service.ReminderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
public class ReminderRestController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/schedule")
    public ResponseEntity<?> scheduleReminder(@RequestBody Map<String, Object> reminderData, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        String type = (String) reminderData.get("reminderType");
        String plate = (String) reminderData.get("plateNumber");
        String date = (String) reminderData.get("appointmentDate");
        String serviceType = (String) reminderData.get("serviceType");

        Reminder reminder;
        if ("EMAIL".equalsIgnoreCase(type)) {
            reminder = new EmailReminder(plate, date, serviceType, user.getEmail(), user.getEmail());
        } else {
            reminder = new DashboardReminder(plate, date, serviceType, user.getEmail());
        }

        reminderService.schedule(reminder);
        return ResponseEntity.ok("Reminder scheduled successfully");
    }

    @GetMapping("/my")
    public ResponseEntity<List<Reminder>> getMyReminders(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(reminderService.findByOwner(user.getEmail()));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelReminder(@RequestParam String plate, @RequestParam String date) {
        reminderService.cancelReminder(plate, date);
        return ResponseEntity.ok("Reminder cancelled");
    }
}
