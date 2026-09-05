package com.libraryapp.entity;

/**
 *  Represents the current status of a borrowing.
 *
 *  BORROWED : the book is currently borrowed by user
 *  RETURNED : the book has been returned
 *  LATE : the expected return date has passed
 */
public enum BorrowingStatus {
    BORROWED("Emprunté"),
    RETURNED("Retourné"),
    LATE("En retard");

    private final String displayName;

    BorrowingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
