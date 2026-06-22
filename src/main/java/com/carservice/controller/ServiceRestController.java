package com.carservice.controller;

import com.carservice.entity.EmergencyRepair;
import com.carservice.entity.ServiceRecord;
import com.carservice.entity.StandardMaintenance;
import com.carservice.entity.User;
import com.carservice.service.ServiceRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ServiceRestController {

    @Autowired
    private ServiceRecordService serviceRecordService;

    @PostMapping("/add")
    public ResponseEntity<?> addService(@RequestBody Map<String, Object> data, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        String plate = (String) data.get("plateNumber");
        String desc = (String) data.get("description");
        String date = (String) data.get("date");
        String type = (String) data.get("serviceType");
        
        // Validation
        if (plate == null || plate.trim().isEmpty()) return ResponseEntity.badRequest().body("Vehicle selection is required");
        if (desc == null || desc.trim().length() < 5) return ResponseEntity.badRequest().body("Work description is too short (min 5 chars)");
        if (date == null || date.isEmpty()) return ResponseEntity.badRequest().body("Service date is required");
        if (Double.parseDouble(data.get("cost").toString()) <= 0) return ResponseEntity.badRequest().body("Base cost must be positive");

        String id = UUID.randomUUID().toString().substring(0, 8);
        double cost = Double.parseDouble(data.get("cost").toString());

        ServiceRecord record;
        if ("EMERGENCY".equalsIgnoreCase(type)) {
            record = new EmergencyRepair(id, plate, desc, date, cost, user.getEmail());
        } else {
            record = new StandardMaintenance(id, plate, desc, date, cost, user.getEmail());
        }

        serviceRecordService.addRecord(record);
        return ResponseEntity.ok("Service record added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getService(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");
        
        ServiceRecord record = serviceRecordService.findById(id);
        if (record == null) return ResponseEntity.notFound().build();
        if (!record.getOwnerEmail().equalsIgnoreCase(user.getEmail())) return ResponseEntity.status(403).body("Access Denied");
        
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable String id, @RequestBody Map<String, Object> data, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        ServiceRecord existing = serviceRecordService.findById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        if (!existing.getOwnerEmail().equalsIgnoreCase(user.getEmail())) return ResponseEntity.status(403).body("Access Denied");

        String plate = (String) data.get("plateNumber");
        String desc = (String) data.get("description");
        String date = (String) data.get("date");
        String type = (String) data.get("serviceType");
        double cost = Double.parseDouble(data.get("cost").toString());

        // Validation
        if (plate == null || plate.trim().isEmpty()) return ResponseEntity.badRequest().body("Vehicle selection is required");
        if (desc == null || desc.trim().length() < 5) return ResponseEntity.badRequest().body("Work description is too short (min 5 chars)");
        if (date == null || date.isEmpty()) return ResponseEntity.badRequest().body("Service date is required");
        if (cost <= 0) return ResponseEntity.badRequest().body("Base cost must be positive");

        ServiceRecord updated;
        if ("EMERGENCY".equalsIgnoreCase(type)) {
            updated = new EmergencyRepair(id, plate, desc, date, cost, user.getEmail());
        } else {
            updated = new StandardMaintenance(id, plate, desc, date, cost, user.getEmail());
        }

        serviceRecordService.updateRecord(updated);
        return ResponseEntity.ok("Record updated successfully");
    }

    @GetMapping("/my")
    public ResponseEntity<List<ServiceRecord>> getMyServiceRecords(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(serviceRecordService.findByOwner(user.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");
        
        ServiceRecord record = serviceRecordService.findById(id);
        if (record != null && record.getOwnerEmail().equalsIgnoreCase(user.getEmail())) {
            serviceRecordService.delete(id);
            return ResponseEntity.ok("Record deleted");
        }
        return ResponseEntity.status(403).body("Unauthorized");
    }

    @GetMapping("/calculate-total/{plate}")
    public ResponseEntity<Double> calculateTotal(@PathVariable String plate) {
        return ResponseEntity.ok(serviceRecordService.calculateVehicleTotalCost(plate));
    }
}
