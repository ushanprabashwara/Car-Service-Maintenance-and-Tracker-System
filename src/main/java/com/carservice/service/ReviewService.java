package com.carservice.service;

import com.carservice.entity.Review;
import com.carservice.entity.VerifiedReview;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private static final String FILE_PATH = "data/reviews.txt";

    public ReviewService() {
        try {
            Files.createDirectories(Paths.get("data"));
            File file = new File(FILE_PATH);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addReview(Review review) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(review.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Review r = parseReviewLine(line);
                if (r != null) reviews.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    /**
     * Parses a review line using the pipe-delimited format.
     * Falls back to legacy comma-delimited parsing for old records.
     */
    private Review parseReviewLine(String line) {
        try {
            // New pipe format: id|username|rating|type|comment
            if (line.contains("|")) {
                String[] parts = line.split("(?<!\\\\)\\|", 5);
                if (parts.length >= 5) {
                    String type = parts[3].trim();
                    Review r = Review.fromCsv(line);
                    if (r == null) return null;
                    return "VERIFIED".equalsIgnoreCase(type)
                            ? new VerifiedReview(r.getId(), r.getUsername(), r.getComment(), r.getRating())
                            : r;
                }
            }
            // Legacy comma format
            return Review.fromLegacyCsv(line);
        } catch (Exception e) {
            // Skip malformed lines — don't crash the whole endpoint
            System.err.println("Skipping malformed review line: " + line);
            return null;
        }
    }

    public void removeReview(String id) {
        List<Review> updated = findAll().stream()
                .filter(r -> !r.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        saveAll(updated);
    }

    private synchronized void saveAll(List<Review> reviews) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Review r : reviews) {
                writer.write(r.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
