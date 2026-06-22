package com.carservice.service;

import com.carservice.entity.Car;
import com.carservice.entity.Motorbike;
import com.carservice.entity.Vehicle;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private static final String FILE_PATH = "data/vehicles.txt";

    public VehicleService() {
        try {
            Files.createDirectories(Paths.get("data"));
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(vehicle.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Vehicle v = parseVehicleCsv(line);
                if (v != null) vehicles.add(v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    private Vehicle parseVehicleCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        String type = parts[4];
        if ("CAR".equalsIgnoreCase(type)) {
            return Car.fromCsv(csv);
        } else if ("MOTORBIKE".equalsIgnoreCase(type)) {
            return Motorbike.fromCsv(csv);
        }
        return null;
    }

    public List<Vehicle> findByOwner(String ownerEmail) {
        return findAll().stream()
                .filter(v -> v.getOwnerEmail().equalsIgnoreCase(ownerEmail))
                .collect(Collectors.toList());
    }

    public Optional<Vehicle> findByPlate(String plateNumber) {
        return findAll().stream()
                .filter(v -> v.getPlateNumber().equalsIgnoreCase(plateNumber))
                .findFirst();
    }

    public void update(Vehicle updatedVehicle) {
        List<Vehicle> all = findAll();
        List<Vehicle> updatedList = all.stream()
                .map(v -> v.getPlateNumber().equalsIgnoreCase(updatedVehicle.getPlateNumber()) ? updatedVehicle : v)
                .collect(Collectors.toList());
        saveAll(updatedList);
    }

    public void delete(String plateNumber) {
        List<Vehicle> updatedList = findAll().stream()
                .filter(v -> !v.getPlateNumber().equalsIgnoreCase(plateNumber))
                .collect(Collectors.toList());
        saveAll(updatedList);
    }

    private synchronized void saveAll(List<Vehicle> vehicles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Vehicle v : vehicles) {
                writer.write(v.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
