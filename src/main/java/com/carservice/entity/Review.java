package com.carservice.entity;

import java.io.Serializable;

public class Review implements Serializable {
    private String id;
    private String username;
    private String comment;
    private int rating;
    private String type; // "STANDARD" or "VERIFIED"

    public Review() {}
    public Review(String id, String username, String comment, int rating, String type) {
        this.id = id;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
        this.type = type;
    }

    public String getId()                  { return id; }
    public void setId(String id)           { this.id = id; }
    public String getUsername()            { return username; }
    public void setUsername(String u)      { this.username = u; }
    public String getComment()             { return comment; }
    public void setComment(String c)       { this.comment = c; }
    public int getRating()                 { return rating; }
    public void setRating(int r)           { this.rating = r; }
    public String getType()                { return type; }
    public void setType(String t)          { this.type = t; }

    public String getDisplayContent() {
        return "Review by " + username + ": " + comment + " (" + rating + "/5 stars)";
    }

    /**
     * Serialize with pipe delimiter so commas inside comments don't break parsing.
     * Format: id|username|rating|type|comment
     * (comment is last so it can safely contain any characters including pipes are escaped)
     */
    public String toCsv() {
        String safeComment = comment == null ? "" : comment.replace("|", "\\|");
        String safeUsername = username == null ? "" : username.replace("|", "\\|");
        return id + "|" + safeUsername + "|" + rating + "|" + type + "|" + safeComment;
    }

    public static Review fromCsv(String csv) {
        if (csv == null || csv.isBlank()) return null;
        // Format: id|username|rating|type|comment
        // Split on unescaped pipes only (limit 5 to keep comment intact)
        String[] parts = csv.split("(?<!\\\\)\\|", 5);
        if (parts.length < 5) return null;
        try {
            String id       = parts[0];
            String username = parts[1].replace("\\|", "|");
            int rating      = Integer.parseInt(parts[2].trim());
            String type     = parts[3].trim();
            String comment  = parts[4].replace("\\|", "|");
            return new Review(id, username, comment, rating, type);
        } catch (NumberFormatException e) {
            // Attempt legacy comma-format migration (old data before the fix)
            return fromLegacyCsv(csv);
        }
    }

    /** Migrate old comma-delimited records written before this fix. */
    public static Review fromLegacyCsv(String csv) {
        try {
            // Format was: id,username,comment...,rating,type
            // rating and type are always the last two tokens
            String[] parts = csv.split(",");
            if (parts.length < 5) return null;
            String type   = parts[parts.length - 1].trim();
            int rating    = Integer.parseInt(parts[parts.length - 2].trim());
            String id     = parts[0];
            String username = parts[1];
            // everything in between is the comment (may have been split by commas)
            StringBuilder commentBuilder = new StringBuilder();
            for (int i = 2; i < parts.length - 2; i++) {
                if (i > 2) commentBuilder.append(",");
                commentBuilder.append(parts[i]);
            }
            return new Review(id, username, commentBuilder.toString().trim(), rating, type);
        } catch (Exception e) {
            return null;
        }
    }
}
