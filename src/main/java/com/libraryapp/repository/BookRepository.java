package com.libraryapp.repository;

import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository used to access Book data from the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(BookCategory category);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}