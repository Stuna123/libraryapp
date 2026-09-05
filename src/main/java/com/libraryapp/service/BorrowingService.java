package com.libraryapp.service;

import com.libraryapp.entity.AppUser;
import com.libraryapp.entity.Book;
import com.libraryapp.entity.Borrowing;
import com.libraryapp.entity.BorrowingStatus;
import com.libraryapp.repository.AppUserRepository;
import com.libraryapp.repository.BookRepository;
import com.libraryapp.repository.BorrowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 *  Service responsible for borrowing-related business operations
 *
 *  It handles book borrowing, stock verification, borrowing duration validation,
 *  retrieval of user borrowing history and book return operations.
 */

@Service
@RequiredArgsConstructor
public class BorrowingService {
    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    private final BorrowingRepository borrowingRepository;

    @Transactional
    public void borrowBook(Long bookId, String userEmail, int numberOfDays) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Le livre n'a pas été trouvé"));

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("L'utilisateur n'a pas été trouvé"));

        boolean alreadyBorrowed = borrowingRepository.existsByUser_EmailAndBook_IdAndStatus(userEmail, bookId, BorrowingStatus.BORROWED);
        if (alreadyBorrowed) {
            throw new RuntimeException("Vous avez déjà emprunté ce livre.");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Aucun exemplaire disponible pour ce livre.");
        }

        if (numberOfDays < 1 || numberOfDays > 30) {
            throw new RuntimeException("La durée d'emprunt doit être comprise entre 1 et 30 jours.");
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(numberOfDays);

        Borrowing borrowing = Borrowing.builder()
                .user(user)
                .book(book)
                .borrowDate(borrowDate)
                .dueDate(dueDate)
                .status(BorrowingStatus.BORROWED)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        borrowingRepository.save(borrowing);
        bookRepository.save(book);
    }

    public List<Borrowing> getBorrowingsByUserEmail(String userEmail) {
        return borrowingRepository.findByUser_Email(userEmail);
    }

    @Transactional
    public void returnBook(Long borrowingId, String userEmail) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("L'emprunt est introuvable"));

        if (!borrowing.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Vous ne pouvez pas retourner cet emprunt.");
        }

        if (borrowing.getStatus() == BorrowingStatus.RETURNED) {
            throw new RuntimeException("Ce livre a déjà été retourné");
        }

        borrowing.setReturnDate(LocalDate.now());
        borrowing.setStatus(BorrowingStatus.RETURNED);

        Book book = borrowing.getBook();
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }

        borrowingRepository.save(borrowing);
        bookRepository.save(book);
    }
}
