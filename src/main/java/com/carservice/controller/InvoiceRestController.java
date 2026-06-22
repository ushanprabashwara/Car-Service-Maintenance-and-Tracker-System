package com.carservice.controller;

import com.carservice.entity.Invoice;
import com.carservice.entity.User;
import com.carservice.service.InvoiceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceRestController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateInvoice(@RequestBody Invoice invoice, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setOwnerEmail(user.getEmail());
        invoice.setStatus("PENDING");
        invoiceService.generateInvoice(invoice);
        return ResponseEntity.ok("Invoice generated successfully");
    }

    @GetMapping("/my")
    public ResponseEntity<List<Invoice>> getMyInvoices(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(invoiceService.findByOwner(user.getEmail()));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payInvoice(@PathVariable String id) {
        invoiceService.updateStatus(id, "PAID");
        return ResponseEntity.ok("Payment processed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Invoice removed");
    }
}
