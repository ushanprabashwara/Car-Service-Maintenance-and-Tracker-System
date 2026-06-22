package com.carservice.service;

import com.carservice.entity.Invoice;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private static final String FILE_PATH = "data/invoices.txt";

    public InvoiceService() {
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

    public synchronized void generateInvoice(Invoice invoice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(invoice.toCsv());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Invoice inv = Invoice.fromCsv(line);
                if (inv != null) invoices.add(inv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<Invoice> findByOwner(String email) {
        return findAll().stream()
                .filter(i -> i.getOwnerEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    public void updateStatus(String invoiceNo, String status) {
        List<Invoice> all = findAll();
        List<Invoice> updated = all.stream()
                .map(i -> i.getInvoiceNumber().equalsIgnoreCase(invoiceNo) ? new Invoice(i.getInvoiceNumber(), i.getPlateNumber(), i.getAmount(), status, i.getOwnerEmail(), i.getDate()) : i)
                .collect(Collectors.toList());
        saveAll(updated);
    }

    public void deleteInvoice(String invoiceNo) {
        List<Invoice> updated = findAll().stream()
                .filter(i -> !i.getInvoiceNumber().equalsIgnoreCase(invoiceNo))
                .collect(Collectors.toList());
        saveAll(updated);
    }

    private synchronized void saveAll(List<Invoice> invoices) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Invoice i : invoices) {
                writer.write(i.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
