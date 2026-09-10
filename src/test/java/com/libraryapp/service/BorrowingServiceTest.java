package com.libraryapp.service;

import com.libraryapp.repository.AppUserRepository;
import com.libraryapp.repository.BookRepository;
import com.libraryapp.repository.BorrowingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.libraryapp.entity.AppUser;
import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import com.libraryapp.entity.Borrowing;
import com.libraryapp.entity.BorrowingStatus;
import com.libraryapp.entity.Role;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    @Test
    void shouldBorrowBookWhenBookIsAvailable() {
        // Arrange
        Long bookId = 1L;
        String userEmail = "francis@test.com";
        int numberOfDays = 14;

        Book book = Book.builder()
                .id(bookId)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category(BookCategory.COMPUTER_SCIENCE)
                .description("Un livre de référence sur l'écriture d'un code propre.")
                .imageUrl("")
                .totalCopies(5)
                .availableCopies(5)
                .build();

        AppUser user = AppUser.builder()
                .id(1L)
                .firstName("Francis")
                .lastName("Tabora")
                .email(userEmail)
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Mockito.doReturn(Optional.of(book))
                .when(bookRepository)
                .findById(bookId);

        Mockito.doReturn(Optional.of(user))
                .when(appUserRepository)
                .findByEmail(userEmail);

        Mockito.doReturn(false)
                .when(borrowingRepository)
                .existsByUser_EmailAndBook_IdAndStatus(userEmail, bookId, BorrowingStatus.BORROWED);

        // Act
        borrowingService.borrowBook(bookId, userEmail, numberOfDays);

        // Assert
        ArgumentCaptor<Borrowing> borrowingCaptor = ArgumentCaptor.forClass(Borrowing.class);

        verify(borrowingRepository).save(borrowingCaptor.capture());
        verify(bookRepository).save(book);

        Borrowing savedBorrowing = borrowingCaptor.getValue();

        assertThat(savedBorrowing.getUser()).isEqualTo(user);
        assertThat(savedBorrowing.getBook()).isEqualTo(book);
        assertThat(savedBorrowing.getStatus()).isEqualTo(BorrowingStatus.BORROWED);
        assertThat(savedBorrowing.getBorrowDate()).isNotNull();
        assertThat(savedBorrowing.getDueDate()).isEqualTo(savedBorrowing.getBorrowDate().plusDays(numberOfDays));

        assertThat(book.getAvailableCopies()).isEqualTo(4);
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyBorrowedBook() {
        // Arrange
        Long bookId = 1L;
        String userEmail = "francis@test.com";
        int numberOfDays = 14;

        Book book = Book.builder()
                .id(bookId)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category(BookCategory.COMPUTER_SCIENCE)
                .description("Un livre de référence sur l'écriture d'un code propre.")
                .imageUrl("")
                .totalCopies(5)
                .availableCopies(5)
                .build();

        AppUser user = AppUser.builder()
                .id(1L)
                .firstName("Francis")
                .lastName("Tabora")
                .email(userEmail)
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Mockito.doReturn(Optional.of(book))
                .when(bookRepository)
                .findById(bookId);

        Mockito.doReturn(Optional.of(user))
                .when(appUserRepository)
                .findByEmail(userEmail);

        Mockito.doReturn(true)
                .when(borrowingRepository)
                .existsByUser_EmailAndBook_IdAndStatus(userEmail, bookId, BorrowingStatus.BORROWED);

        // Act & Assert
        assertThatThrownBy(() -> borrowingService.borrowBook(bookId, userEmail, numberOfDays))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Vous avez déjà emprunté ce livre.");

        verify(borrowingRepository, never()).save(any(Borrowing.class));
        verify(bookRepository, never()).save(any(Book.class));

        assertThat(book.getAvailableCopies()).isEqualTo(5);
    }

    @Test
    void shouldReturnBookAndIncreaseAvailableCopies() {
        // Arrange
        Long borrowingId = 1L;
        String userEmail = "francis@test.com";

        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category(BookCategory.COMPUTER_SCIENCE)
                .description("Un livre de référence sur l'écriture d'un code propre.")
                .imageUrl("")
                .totalCopies(5)
                .availableCopies(4)
                .build();

        AppUser user = AppUser.builder()
                .id(1L)
                .firstName("Francis")
                .lastName("Tabora")
                .email(userEmail)
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Borrowing borrowing = Borrowing.builder()
                .id(borrowingId)
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now().minusDays(3))
                .dueDate(LocalDate.now().plusDays(11))
                .status(BorrowingStatus.BORROWED)
                .build();

        Mockito.doReturn(Optional.of(borrowing))
                .when(borrowingRepository)
                .findById(borrowingId);

        // Act
        borrowingService.returnBook(borrowingId, userEmail);

        // Assert
        assertThat(borrowing.getStatus()).isEqualTo(BorrowingStatus.RETURNED);
        assertThat(borrowing.getReturnDate()).isNotNull();
        assertThat(book.getAvailableCopies()).isEqualTo(5);

        verify(borrowingRepository).save(borrowing);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldThrowExceptionWhenNoAvailableCopies() {
        // Arrange
        Long bookId = 1L;
        String userEmail = "francis@test.com";
        int numberOfDays = 14;

        Book book = Book.builder()
                .id(bookId)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category(BookCategory.COMPUTER_SCIENCE)
                .description("Un livre de référence sur l'écriture d'un code propre.")
                .imageUrl("")
                .totalCopies(5)
                .availableCopies(0)
                .build();

        AppUser user = AppUser.builder()
                .id(1L)
                .firstName("Francis")
                .lastName("Tabora")
                .email(userEmail)
                .password("encoded-password")
                .role(Role.USER)
                .build();

        Mockito.doReturn(Optional.of(book))
                .when(bookRepository)
                .findById(bookId);

        Mockito.doReturn(Optional.of(user))
                .when(appUserRepository)
                .findByEmail(userEmail);

        Mockito.doReturn(false)
                .when(borrowingRepository)
                .existsByUser_EmailAndBook_IdAndStatus(userEmail, bookId, BorrowingStatus.BORROWED);

        // Act & Assert
        assertThatThrownBy(() -> borrowingService.borrowBook(bookId, userEmail, numberOfDays))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Aucun exemplaire disponible pour ce livre.");

        verify(borrowingRepository, never()).save(any(Borrowing.class));
        verify(bookRepository, never()).save(any(Book.class));

        assertThat(book.getAvailableCopies()).isEqualTo(0);
    }
}