package com.libraryapp.service;

import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import com.libraryapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  Service responsible for book-related business operations.
 *
 *  It centralizes the logic used to retrieve books, filter them by category
 *  and get the details of a specific book
 */
@Service
@RequiredArgsConstructor

public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id : " +id));
    }

    public List<Book> getBooksByCategory(BookCategory category) {
        return bookRepository.findByCategory(category);
    }

    public BookCategory[] getAllCategories() {
        return BookCategory.values();
    }
}
