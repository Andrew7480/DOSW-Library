package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
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
        Book created = bookService.addBook("Clean Code", "Robert C. Martin", 3);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Clean Code", created.getTitle());
        assertEquals("Robert C. Martin", created.getAuthor());
        assertEquals(3, bookService.getAvailableCopies(created.getId()));
    }

    @Test
    void getAllBooksShouldReturnAllRegisteredBooks() {
        bookService.addBook("Book 1", "Author 1", 2);
        bookService.addBook("Book 2", "Author 2", 1);

        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void getBookByIdShouldReturnExistingAvailableBook() {
        Book created = bookService.addBook("Domain-Driven Design", "Eric Evans", 2);

        Book found = bookService.getBookById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("Domain-Driven Design", found.getTitle());
    }

    @Test
    void getBookByIdShouldThrowWhenBookDoesNotExist() {
        assertThrows(BookNotAvailableException.class, () -> bookService.getBookById("missing-id"));
    }

    @Test
    void updateAvailabilityShouldChangeAvailableCopies() {
        Book created = bookService.addBook("Refactoring", "Martin Fowler", 1);

        bookService.updateAvailability(created.getId(), 5);

        assertEquals(5, bookService.getAvailableCopies(created.getId()));
        assertTrue(bookService.isBookAvailable(created.getId()));
    }
}
