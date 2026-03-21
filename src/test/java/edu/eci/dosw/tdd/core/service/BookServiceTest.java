package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import edu.eci.dosw.tdd.core.exception.BookAlreadyExitsException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService();
    }

    @Test
    void addBookShouldCreateBookWithGeneratedIdAndCopies() {
        String title = "Clean Code";
        String author = "Robert C. Martin";
        int copies = 3;

        Book created = bookService.addBook(title, author, copies);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Clean Code", created.getTitle());
        assertEquals("Robert C. Martin", created.getAuthor());
        assertEquals(3, bookService.getAvailableCopies(title));
    }

    @Test
    void getAllBooksShouldReturnAllRegisteredBooks() {
        bookService.addBook("Book 1", "Author 1", 2);
        bookService.addBook("Book 2", "Author 2", 1);

        int totalBooks = bookService.getAllBooks().size();

        assertEquals(2, totalBooks);
    }

    @Test
    void getBookByTitleShouldReturnExistingBook() {
        String title = "Domain-Driven Design";
        Book created = bookService.addBook(title, "Eric Evans", 2);

        Book found = bookService.getBook(title);

        assertEquals(created.getId(), found.getId());
        assertEquals(title, found.getTitle());
    }

    @Test
    void getBookByTitleShouldThrowWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> bookService.getBook("NonExistent"));
    }

    @Test
    void addBookShouldThrowWhenTitleIsDuplicate() {        
        String title = "Clean Code";
        bookService.addBook(title, "Robert C. Martin", 3);

        assertThrows(BookAlreadyExitsException.class, () -> bookService.addBook(title, "Another Author", 2));
    }

    @Test
    void getAvailableCopiesShouldReturnCorrectAmount() {
        String title = "Refactoring";
        bookService.addBook(title, "Martin Fowler", 5);

        int copies = bookService.getAvailableCopies(title);

        assertEquals(5, copies);
    }

    @Test
    void getAvailableBooksShouldReturnOnlyBooksWithCopies() {
        bookService.addBook("Available Book", "Author 1", 2);
        bookService.addBook("Another Available Book", "Author 2", 1);

        int availableCount = bookService.getAvailableBooks().size();

        assertEquals(2, availableCount);
    }

    @Test
    void isBookAvailableShouldReturnTrueWhenCopiesGreaterThanZero() {
        String title = "Design Patterns";
        bookService.addBook(title, "Gang of Four", 3);

        boolean available = bookService.isBookAvailable(title);

        assertTrue(available);
    }

    @Test
    void isBookAvailableShouldThrowWhenBookDoesNotExist() {

        assertThrows(BookNotFoundException.class, () -> bookService.isBookAvailable("Missing"));
    }

    @Test
    void updateAvailabilityShouldChangeAvailableCopies() {
        String title = "Test Book";
        bookService.addBook(title, "Test Author", 2);

        bookService.updateAvailability(title, 5);

        assertEquals(5, bookService.getAvailableCopies(title));
    }

    @Test
    void updateAvailabilityShouldThrowWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> bookService.updateAvailability("Missing", 10));
    }

    @Test
    void increaseAvailableCopiesShouldAddCopiesToBook() {
        String title = "Clean Code";
        bookService.addBook(title, "Robert C. Martin", 2);

        bookService.increaseAvailableCopies(title, 3);

        assertEquals(5, bookService.getAvailableCopies(title));
    }

    @Test
    void increaseAvailableCopiesShouldThrowWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> bookService.increaseAvailableCopies("Missing", 1));
    }

    @Test
    void decreaseAvailableCopiesShouldSubtractCopiesWhenEnoughStock() {
        String title = "Refactoring";
        bookService.addBook(title, "Martin Fowler", 5);

        bookService.decreaseAvailableCopies(title, 2);

        assertEquals(3, bookService.getAvailableCopies(title));
    }

    @Test
    void decreaseAvailableCopiesShouldThrowWhenCopiesRequestedExceedAvailable() {
        String title = "Domain-Driven Design";
        bookService.addBook(title, "Eric Evans", 1);

        assertThrows(BookNotAvailableException.class, () -> bookService.decreaseAvailableCopies(title, 2));
    }

    @Test
    void decreaseAvailableCopiesShouldThrowWhenBookDoesNotExist() {
        assertThrows(BookNotFoundException.class, () -> bookService.decreaseAvailableCopies("Missing", 1));
    }

    @Test
    void getAllBooksWithCopiesShouldReturnCopyOfInternalMap() {
        Book book1 = bookService.addBook("Book 1", "Author 1", 2);
        Book book2 = bookService.addBook("Book 2", "Author 2", 4);

        Map<Book, Integer> allBooksWithCopies = bookService.getAllBooksWithCopies();

        assertEquals(2, allBooksWithCopies.size());
        assertEquals(2, allBooksWithCopies.get(book1));
        assertEquals(4, allBooksWithCopies.get(book2));
    }

    @Test
    void getAllBooksWithCopiesShouldReturnDefensiveCopy() {
        Book book = bookService.addBook("Book 1", "Author 1", 2);
        Map<Book, Integer> allBooksWithCopies = bookService.getAllBooksWithCopies();

        allBooksWithCopies.put(book, 99);

        assertEquals(2, bookService.getAvailableCopies("Book 1"));
    }
}
