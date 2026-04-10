package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.exception.BookAlreadyExitsException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBookShouldCreateBookWithGeneratedIdAndCopies() {
        String title = "Clean Code";
        String author = "Robert C. Martin";
        int copies = 3;

        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.empty());

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        Book created = bookService.addBook(title, author, copies);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Clean Code", created.getTitle());
        assertEquals("Robert C. Martin", created.getAuthor());
        verify(bookRepository).save(captor.capture());
        assertEquals(3, captor.getValue().getAvailableStock());
    }

    @Test
    void getAllBooksShouldReturnAllRegisteredBooks() {
        Book book1 = new Book("id1", "Book 1", "Author 1", 2, 2);
        Book book2 = new Book("id2", "Book 2", "Author 2", 1, 1);
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        int totalBooks = bookService.getAllBooks().size();
        assertEquals(2, totalBooks);
    }

    @Test
    void getBookByTitleShouldReturnExistingBook() {
        String title = "Domain-Driven Design";
        Book book = new Book("id1", title, "Eric Evans", 2, 2);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        Book found = bookService.getBook(title);
        assertEquals("id1", found.getId());
        assertEquals(title, found.getTitle());
    }

    @Test
    void getBookByTitleShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findByTitleIgnoreCase("NonExistent")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBook("NonExistent"));
    }

    @Test
    void addBookShouldThrowWhenTitleIsDuplicate() {
        String title = "Clean Code";
        Book book = new Book("id1", title, "Robert C. Martin", 3, 2);
        when(bookRepository.findByTitleIgnoreCase(title.trim().toLowerCase())).thenReturn(Optional.of(book));
        assertThrows(BookAlreadyExitsException.class, () -> bookService.addBook(title, "Another Author", 2));
    }

    @Test
    void getAvailableCopiesShouldReturnCorrectAmount() {
        String title = "Refactoring";
        Book book = new Book("id1", title, "Martin Fowler", 5, 5);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        int copies = bookService.getAvailableCopies(title);
        assertEquals(5, copies);
    }

    @Test
    void getAvailableBooksShouldReturnOnlyBooksWithCopies() {
        Book book1 = new Book("id1", "Available Book", "Author 1", 2, 2);
        Book book2 = new Book("id2", "Another Available Book", "Author 2", 1, 1);
        when(bookRepository.findAllByAvailableStockGreaterThan(0)).thenReturn(List.of(book1, book2));
        int availableCount = bookService.getAvailableBooks().size();
        assertEquals(2, availableCount);
    }

    @Test
    void isBookAvailableShouldReturnTrueWhenCopiesGreaterThanZero() {
        String title = "Design Patterns";
        Book book = new Book("id1", title, "Gang of Four", 3, 3);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        boolean available = bookService.isBookAvailable(title);
        assertTrue(available);
    }

    @Test
    void isBookAvailableShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findByTitleIgnoreCase("Missing")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.isBookAvailable("Missing"));
    }

    @Test
    void updateAvailabilityShouldChangeAvailableCopies() {
        String title = "Test Book";
        Book book = new Book("id1", title, "Test Author", 2, 2);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        bookService.updateAvailability(title, 5);
        verify(bookRepository).save(argThat(e -> e.getAvailableStock() == 5));
    }

    @Test
    void updateAvailabilityShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findByTitleIgnoreCase("Missing")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.updateAvailability("Missing", 10));
    }

    @Test
    void increaseAvailableCopiesShouldAddCopiesToBook() {
        String title = "Clean Code";
        Book book = new Book("id1", title, "Robert C. Martin", 2, 2);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        bookService.increaseAvailableCopies(title, 3);
        verify(bookRepository).save(argThat(e -> e.getAvailableStock() == 5));
    }

    @Test
    void increaseAvailableCopiesShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findByTitleIgnoreCase("Missing")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.increaseAvailableCopies("Missing", 1));
    }

    @Test
    void decreaseAvailableCopiesShouldSubtractCopiesWhenEnoughStock() {
        String title = "Refactoring";
        Book book = new Book("id1", title, "Martin Fowler", 5, 5);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        bookService.decreaseAvailableCopies(title, 2);
        verify(bookRepository).save(argThat(e -> e.getAvailableStock() == 3));
    }

    @Test
    void decreaseAvailableCopiesShouldThrowWhenCopiesRequestedExceedAvailable() {
        String title = "Domain-Driven Design";
        Book book = new Book("id1", title, "Eric Evans", 1, 1);
        when(bookRepository.findByTitleIgnoreCase(title)).thenReturn(Optional.of(book));
        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseAvailableCopies(title, 2));
    }

    @Test
    void decreaseAvailableCopiesShouldThrowWhenBookDoesNotExist() {
        when(bookRepository.findByTitleIgnoreCase("Missing")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.decreaseAvailableCopies("Missing", 1));
    }
}
