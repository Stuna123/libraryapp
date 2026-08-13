package com.libraryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *  Represents a book borrowing made by a user.
 *
 *  A borrowing links one user to one book and stores the borrowing date,
 *  the expected return date, the actual return date and the current status.
 */

@Entity
@Table(name = "borrowings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Borrowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        if(this.status == null) {
            this.status = BorrowingStatus.BORROWED;
        }
    }
}
