package com.carservice.controller;

import com.carservice.entity.Review;
import com.carservice.entity.User;
import com.carservice.entity.VerifiedReview;
import com.carservice.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody Review review, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("Not logged in");

        review.setId(UUID.randomUUID().toString().substring(0, 8));
        review.setUsername(user.getFullName()); // Using full name as display name
        
        reviewService.addReview(new VerifiedReview(review.getId(), review.getUsername(), review.getComment(), review.getRating()));
        return ResponseEntity.ok("Review added successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeReview(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Administrator privileges required");
        }
        reviewService.removeReview(id);
        return ResponseEntity.ok("Review removed");
    }
}
