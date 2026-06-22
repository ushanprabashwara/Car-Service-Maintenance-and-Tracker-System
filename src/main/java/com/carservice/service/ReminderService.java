package com.carservice.service;

import com.carservice.entity.DashboardReminder;
import com.carservice.entity.EmailReminder;
import com.carservice.entity.Reminder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    private static final String FILE_PATH = "data/reminders.txt";

    public ReminderService() {
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

    public synchronized void schedule(Reminder reminder) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(reminder.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Reminder> findAll() {
        List<Reminder> reminders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Reminder r = parseReminderCsv(line);
                if (r != null) reminders.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reminders;
    }

    private Reminder parseReminderCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        String type = parts[4];
        if ("EMAIL".equalsIgnoreCase(type)) {
            return EmailReminder.fromCsv(csv);
        } else if ("DASHBOARD".equalsIgnoreCase(type)) {
            return DashboardReminder.fromCsv(csv);
        }
        return null;
    }

    public List<Reminder> findByOwner(String email) {
        return findAll().stream()
                .filter(r -> r.getOwnerEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    public void cancelReminder(String plate, String date) {
        List<Reminder> updated = findAll().stream()
                .filter(r -> !(r.getPlateNumber().equalsIgnoreCase(plate) && r.getAppointmentDate().equals(date)))
                .collect(Collectors.toList());
        saveAll(updated);
    }

    private synchronized void saveAll(List<Reminder> reminders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Reminder r : reminders) {
                writer.write(r.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
