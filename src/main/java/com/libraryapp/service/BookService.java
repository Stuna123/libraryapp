package com.libraryapp.service;

import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import com.libraryapp.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.libraryapp.form.BookForm;
import org.springframework.transaction.annotation.Transactional;
import com.libraryapp.repository.BorrowingRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 *  Service responsible for book-related business operations.
 *
 *  It centralizes the logic used to retrieve books, filter them by category,
 *  get the details of a specific book and create new books from the admin area.
 */
@Service
@RequiredArgsConstructor

public class BookService {
    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Book not found with id : " + id)
                );
    }

    public List<Book> getBooksByCategory(BookCategory category) {
        return bookRepository.findByCategory(category);
    }

    public BookCategory[] getAllCategories() {
        return BookCategory.values();
    }

    @Transactional
    public void createBook(BookForm form) {
        if (bookRepository.existsByIsbn(form.getIsbn())) {
            throw new RuntimeException("Un livre existe déjà avec cet ISBN.");
        }

        Book book = Book.builder()
                .title(form.getTitle())
                .author(form.getAuthor())
                .isbn(form.getIsbn())
                .category(form.getCategory())
                .description(form.getDescription())
                .imageUrl(form.getImageUrl())
                .totalCopies(form.getTotalCopies())
                .availableCopies(form.getTotalCopies())
                .build();

        bookRepository.save(book);
    }

    public BookForm getBookFormById(Long id) {
        Book book = getBookById(id);

        BookForm form = new BookForm();
        form.setTitle(book.getTitle());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());
        form.setCategory(book.getCategory());
        form.setDescription(book.getDescription());
        form.setImageUrl(book.getImageUrl());
        form.setTotalCopies(book.getTotalCopies());

        return form;
    }

    @Transactional
    public void updateBook(Long id, BookForm form) {
        Book book = getBookById(id);

        Optional<Book> existingBookWithIsbn = bookRepository.findByIsbn(form.getIsbn());

        if (existingBookWithIsbn.isPresent() && !existingBookWithIsbn.get().getId().equals(id)) {
            throw new RuntimeException("Un autre livre existe déjà avec cet ISBN.");
        }

        int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();


        if (form.getTotalCopies() < borrowedCopies) {
            throw new RuntimeException("Le nombre total d'exemplaires ne peut pas être inférieur au nombre d'exemplaires actuellement empruntés.");
        }

        book.setTitle(form.getTitle());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        book.setCategory(form.getCategory());
        book.setDescription(form.getDescription());
        book.setImageUrl(form.getImageUrl());
        book.setTotalCopies(form.getTotalCopies());
        book.setAvailableCopies(form.getTotalCopies() - borrowedCopies);

        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        boolean hasBorrowings = borrowingRepository.existsByBook_Id(id);

        if (hasBorrowings) {
            throw new RuntimeException("Impossible de supprimer ce livre car il possède déjà un historique d'emprunt.");
        }

        bookRepository.delete(book);
    }
}
