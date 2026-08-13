package com.libraryapp.entity;

/**
 *  Represents the current status of a borrowing.
 *
 *  BORROWED : the book is currently borrowed by user
 *  RETURNED : the book has been returned
 *  LATE : the expected return date has passed
 */
public enum BorrowingStatus {
    BORROWED,
    RETURNED,
    LATE
}
