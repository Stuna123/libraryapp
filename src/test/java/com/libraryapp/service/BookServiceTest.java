package com.libraryapp.service;

import com.libraryapp.entity.Book;
import com.libraryapp.entity.BookCategory;
import com.libraryapp.form.BookForm;
import com.libraryapp.repository.BookRepository;
import com.libraryapp.repository.BorrowingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldCreateBookWhenIsbnDoesNotExist() {
        // Arrange
        BookForm form = new BookForm();
        form.setTitle("Clean Code");
        form.setAuthor("Robert C. Martin");
        form.setIsbn("9780132350884");
        form.setCategory(BookCategory.COMPUTER_SCIENCE);
        form.setDescription("Un livre de référence sur l'écriture d'un code propre.");
        form.setImageUrl("");
        form.setTotalCopies(5);

        // Mockito.when(bookRepository.existsByIsbn("9780132350884")).thenReturn(false); .\mvnw test
        Mockito.doReturn(false)
                .when(bookRepository)
                .existsByIsbn("9780132350884");
        // Act
        bookService.createBook(form);

        // Assert
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();

        assertThat(savedBook.getTitle()).isEqualTo("Clean Code");
        assertThat(savedBook.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(savedBook.getIsbn()).isEqualTo("9780132350884");
        assertThat(savedBook.getCategory()).isEqualTo(BookCategory.COMPUTER_SCIENCE);
        assertThat(savedBook.getDescription()).isEqualTo("Un livre de référence sur l'écriture d'un code propre.");
        assertThat(savedBook.getImageUrl()).isEqualTo("");
        assertThat(savedBook.getTotalCopies()).isEqualTo(5);
        assertThat(savedBook.getAvailableCopies()).isEqualTo(5);
    }

    @Test
    void shouldThrowExceptionWhenIsbnAlreadyExists() {
        // Arrange
        BookForm form = new BookForm();
        form.setTitle("Clean Code");
        form.setAuthor("Robert C. Martin");
        form.setIsbn("9780132350884");
        form.setCategory(BookCategory.COMPUTER_SCIENCE);
        form.setDescription("Un livre de référence sur l'écriture d'un code propre");
        form.setImageUrl("");
        form.setTotalCopies(5);

        Mockito.doReturn(true)
                .when(bookRepository)
                .existsByIsbn("9780132350884");

        // Act & Assert
        assertThatThrownBy(() -> bookService.createBook(form))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Un livre existe déjà avec cet ISBN.");

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingBookWithBorrowingHistory() {
        // Arrange
        Long bookId = 1L;

        Book book = Book.builder()
                .id(bookId)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .category(BookCategory.COMPUTER_SCIENCE)
                .description("Un livre de référence sur l'écriture d'un code propre.")
                .imageUrl("")
                .totalCopies(5)
                .availableCopies(3)
                .build();

        Mockito.doReturn(Optional.of(book))
                .when(bookRepository)
                .findById(bookId);

        Mockito.doReturn(true)
                .when(borrowingRepository)
                .existsByBook_Id(bookId);

        // Act & Assert
        assertThatThrownBy(() -> bookService.deleteBook(bookId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Impossible de supprimer ce livre car il possède déjà un historique d'emprunt.");

        verify(bookRepository, never()).delete(any(Book.class));
    }
}