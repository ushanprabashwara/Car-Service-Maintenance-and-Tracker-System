package com.carservice.service;

import com.carservice.entity.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String FILE_PATH = "data/users.txt";

    public UserService() {
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

    public synchronized void register(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromCsv(line);
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public void update(User updatedUser) {
        List<User> allUsers = findAll();
        List<User> updatedList = allUsers.stream()
                .map(u -> u.getEmail().equals(updatedUser.getEmail()) ? updatedUser : u)
                .collect(Collectors.toList());
        saveAll(updatedList);
    }

    public void delete(String email) {
        List<User> updatedList = findAll().stream()
                .filter(u -> !u.getEmail().equals(email))
                .collect(Collectors.toList());
        saveAll(updatedList);
    }

    private synchronized void saveAll(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                writer.write(user.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> login(String email, String password) {
        return findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }
}
