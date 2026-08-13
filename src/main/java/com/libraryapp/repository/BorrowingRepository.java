package com.libraryapp.repository;

import com.libraryapp.entity.Borrowing;
import com.libraryapp.entity.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  Repository used to access Borrowing data from the database.
 */

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByUser_Email(String email);

    List<Borrowing> findByBook_Id(String bookId);

    List<Borrowing> findByStatus(BorrowingStatus status);

}
