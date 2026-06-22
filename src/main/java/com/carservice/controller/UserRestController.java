package com.carservice.controller;

import com.carservice.entity.User;
import com.carservice.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.status(400).body("Invalid email format");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return ResponseEntity.status(400).body("Password must be at least 6 characters long");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email already registered");
        }
        user.setRole("USER");
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        Optional<User> user = userService.login(email, password);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("Not logged in");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updated, HttpSession session) {
        User current = (User) session.getAttribute("user");
        if (current == null) return ResponseEntity.status(401).body("Not logged in");

        // --- THE FIX: Copy OVER ALL missing data from the current session ---
        updated.setEmail(current.getEmail());
        updated.setRole(current.getRole());
        updated.setPassword(current.getPassword()); // <--- CRITICAL FIX

        // If your User entity has an ID field, copy that too!
        // updated.setId(current.getId());

        userService.update(updated);
        session.setAttribute("user", updated);
        return ResponseEntity.ok("Profile updated");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(HttpSession session) {
        User current = (User) session.getAttribute("user");
        if (current == null) return ResponseEntity.status(401).body("Not logged in");
        
        userService.delete(current.getEmail());
        session.invalidate();
        return ResponseEntity.ok("Account deleted");
    }


}