package edu.eci.dosw.tdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService);
    }

    @Test
    void addBookShouldReturnCreatedBook() {
        Book created = new Book( "b-1", "Clean Code", "Robert C. Martin", 3);
        when(bookService.addBook("Clean Code", "Robert C. Martin", 3)).thenReturn(created);

        ResponseEntity<BookDTO> response = bookController.addBook(new BookDTO("b-1", "Clean Code", "Robert C. Martin", 3));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals("Clean Code", response.getBody().getTitle());
        assertEquals(3, response.getBody().getStock());
        
        verify(bookService).addBook("Clean Code", "Robert C. Martin", 3);
    }

    @Test
    void getAllBooksShouldReturnAllBooks() {
        Book book1 = new Book("b-1", "Book 1", "Author 1", 3);
        Book book2 = new Book("b-2", "Book 2", "Author 2", 2);
        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));


        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("b-1", response.getBody().get(0).getId());
        assertEquals(3, response.getBody().get(0).getStock());
        assertEquals("b-2", response.getBody().get(1).getId());
        assertEquals(2, response.getBody().get(1).getStock());
        
        verify(bookService).getAllBooks();
    }

    @Test
    void getBookByTitleShouldReturnBookWhenExists() {
        String title = "DDD";
        Book book = new Book("b-1", title, "Eric Evans", 3);
        when(bookService.getBook(title)).thenReturn(book);

        ResponseEntity<BookDTO> response = bookController.getBookByTitle(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals(title, response.getBody().getTitle());
        assertEquals(3, response.getBody().getStock());
        
        verify(bookService).getBook(title);
    }

    @Test
    void getBookByTitleShouldThrowWhenBookDoesNotExist() {
        String title = "missing";
        when(bookService.getBook(title)).thenThrow(new BookNotFoundException(title));

        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> bookController.getBookByTitle(title));
        
        assertNotNull(ex.getMessage());
        verify(bookService).getBook(title);
    }

    @Test
    void getAvailableCopiesShouldReturnCorrectAmount() {
        String title = "Refactoring";
        when(bookService.getAvailableCopies(title)).thenReturn(5);

        ResponseEntity<Integer> response = bookController.getAvailableCopies(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
        
        verify(bookService).getAvailableCopies(title);
    }

    @Test
    void isBookAvailableShouldReturnTrueWhenCopiesExist() {
        String title = "Design Patterns";
        when(bookService.isBookAvailable(title)).thenReturn(true);

        ResponseEntity<Boolean> response = bookController.isBookAvailable(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        
        verify(bookService).isBookAvailable(title);
    }

    @Test
    void getBooksByAuthorShouldReturnBooksForThatAuthor() {
        String author = "Robert C. Martin";
        Book book1 = new Book("b-1", "Clean Code", author, 3);
        Book book2 = new Book("b-2", "Clean Architecture", author, 2);
        when(bookService.getAllBooksByAuthor(author)).thenReturn(List.of(book1, book2));

        ResponseEntity<List<BookDTO>> response = bookController.getBooksByAuthor(author);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        
        verify(bookService).getAllBooksByAuthor(author);
    }

    @Test
    void getAvailableBooksShouldReturnOnlyAvailableBooks() {
        Book book1 = new Book("b-1", "Available", "Author 1", 1);
        when(bookService.getAvailableBooks()).thenReturn(List.of(book1));

        ResponseEntity<List<BookDTO>> response = bookController.getAvailableBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        
        verify(bookService).getAvailableBooks();
    }

    @Test
    void increaseAvailableCopiesShouldUpdateCopiesSuccessfully() {
        String title = "Clean Code";
        int copies = 2;
        doNothing().when(bookService).increaseAvailableCopies(title, copies);

        ResponseEntity<Void> response = bookController.increaseAvailableCopies(title, copies);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookService).increaseAvailableCopies(title, copies);
    }

    @Test
    void decreaseAvailableCopiesShouldUpdateCopiesSuccessfully() {
        String title = "Domain-Driven Design";
        int copies = 1;
        doNothing().when(bookService).decreaseAvailableCopies(title, copies);

        ResponseEntity<Void> response = bookController.decreaseAvailableCopies(title, copies);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookService).decreaseAvailableCopies(title, copies);
    }

    @Test
    void updateBookShouldUpdateAvailabilityBook() {
        String title = "Old Title";
        int newCopies = 5;
        doNothing().when(bookService).updateAvailability(title, newCopies);

        ResponseEntity<Void> response = bookController.updateAvailabilityBook(title, newCopies);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookService).updateAvailability(title, newCopies);
    }
}
