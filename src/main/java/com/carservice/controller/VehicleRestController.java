package com.carservice.controller;

import com.carservice.entity.Car;
import com.carservice.entity.Motorbike;
import com.carservice.entity.User;
import com.carservice.entity.Vehicle;
import com.carservice.service.VehicleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController

// We use a single level mapping here to prevent 404 "nesting" errors
@RequestMapping("/api/vehicles")
public class VehicleRestController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/my")
    public ResponseEntity<List<Vehicle>> getMyVehicles(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        // This is where we verify if the service is actually returning data
        List<Vehicle> list = vehicleService.findByOwner(user.getEmail());

        System.out.println("DEBUG: Found " + list.size() + " vehicles for " + user.getEmail());

        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView addVehicle(

            @RequestParam("vehicleType") String type,
            @RequestParam("plateNumber") String plate,
            @RequestParam("model") String model,
            @RequestParam("year") int year,
            @RequestParam(value = "numberOfDoors", defaultValue = "4") int doors,
            @RequestParam(value = "hasSidecar", defaultValue = "false") boolean sidecar,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null)
            return new ModelAndView("redirect:/login");

        Vehicle vehicle = "CAR".equalsIgnoreCase(type)
                ? new Car(plate, model, year, user.getEmail(), doors)
                : new Motorbike(plate, model, year, user.getEmail(), sidecar);

        if (vehicle.getPlateNumber() != null) {
            vehicleService.addVehicle(vehicle);
        }

        return new ModelAndView("redirect:/dashboard");
    }

    @DeleteMapping("/{plate}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String plate, HttpSession session) {

        // 1. Security Check: Are they logged in?
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        try {
            // 2. Security Check: Does this vehicle actually belong to them?
            Optional<Vehicle> vehicleOpt = vehicleService.findByPlate(plate);

            if (vehicleOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Vehicle not found");
            }

            if (!vehicleOpt.get().getOwnerEmail().equalsIgnoreCase(user.getEmail())) {
                return ResponseEntity.status(403).body("You are not authorized to delete this vehicle");
            }

            // 3. If all checks pass, delete the vehicle
            vehicleService.delete(plate);
            System.out.println("DEBUG: Vehicle " + plate + " deleted by " + user.getEmail());

            return ResponseEntity.ok("Vehicle deleted successfully");

        } catch (Exception e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal server error while deleting");
        }
    }

    @GetMapping("/{plate}/summary")
    public ResponseEntity<String> getVehicleSummary(@PathVariable String plate, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        Optional<Vehicle> vehicleOpt = vehicleService.findByPlate(plate);
        if (vehicleOpt.isEmpty()) return ResponseEntity.status(404).body("Vehicle not found");
        if (!vehicleOpt.get().getOwnerEmail().equalsIgnoreCase(user.getEmail()))
            return ResponseEntity.status(403).body("Access denied");

        return ResponseEntity.ok(vehicleOpt.get().getVehicleSummary());
    }
}