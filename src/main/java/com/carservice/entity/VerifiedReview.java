package com.carservice.entity;

public class VerifiedReview extends Review {

    public VerifiedReview() { super(); }
    public VerifiedReview(String id, String username, String comment, int rating) {
        super(id, username, comment, rating, "VERIFIED");
    }

    @Override
    public String getDisplayContent() {
        return "⭐ VERIFIED Review by " + getUsername() + ": " + getComment() + " [" + getRating() + "/5]";
    }

    public static VerifiedReview fromCsvVerified(String csv) {
        Review r = Review.fromCsv(csv);
        if (r == null) return null;
        return new VerifiedReview(r.getId(), r.getUsername(), r.getComment(), r.getRating());
    }
}
