package com.carservice.service;

import com.carservice.entity.EmergencyRepair;
import com.carservice.entity.ServiceRecord;
import com.carservice.entity.StandardMaintenance;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRecordService {
    private static final String FILE_PATH = "data/services.txt";

    public ServiceRecordService() {
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

    public synchronized void addRecord(ServiceRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(record.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ServiceRecord> findAll() {
        List<ServiceRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ServiceRecord r = parseServiceCsv(line);
                if (r != null) records.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private ServiceRecord parseServiceCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 7) return null; // recordId, plateNumber, description, date, baseCost, ownerEmail, type
        
        String type = parts[6];
        if ("EMERGENCY".equalsIgnoreCase(type)) {
            return new EmergencyRepair(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), parts[5]);
        } else {
            return new StandardMaintenance(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), parts[5]);
        }
    }

    public ServiceRecord findById(String id) {
        return findAll().stream()
                .filter(r -> r.getRecordId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public List<ServiceRecord> findByOwner(String email) {
        return findAll().stream()
                .filter(r -> r.getOwnerEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    public List<ServiceRecord> findByVehicle(String plate) {
        return findAll().stream()
                .filter(r -> r.getPlateNumber().equalsIgnoreCase(plate))
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        List<ServiceRecord> updated = findAll().stream()
                .filter(r -> !r.getRecordId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        saveAll(updated);
    }

    public void updateRecord(ServiceRecord updatedRecord) {
        List<ServiceRecord> all = findAll();
        List<ServiceRecord> updatedList = all.stream()
                .map(r -> r.getRecordId().equalsIgnoreCase(updatedRecord.getRecordId()) ? updatedRecord : r)
                .collect(Collectors.toList());
        saveAll(updatedList);
    }

    public double calculateVehicleTotalCost(String plate) {
        return findByVehicle(plate).stream()
                .mapToDouble(ServiceRecord::calculateTotalCost)
                .sum();
    }

    private synchronized void saveAll(List<ServiceRecord> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (ServiceRecord r : list) {
                writer.write(r.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
