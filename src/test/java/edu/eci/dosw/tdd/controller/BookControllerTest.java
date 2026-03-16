package edu.eci.dosw.tdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
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
        Book created = new Book("Clean Code", "Robert C. Martin", "b-1");
        when(bookService.addBook("Clean Code", "Robert C. Martin", 3)).thenReturn(created);

        ResponseEntity<BookDTO> response = bookController.addBook(new BookDTO("Clean Code", "Robert C. Martin", 3));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals("Clean Code", response.getBody().getTitle());
        assertEquals(3, response.getBody().getCopies());
        verify(bookService).addBook("Clean Code", "Robert C. Martin", 3);
    }

    @Test
    void getAllBooksShouldReturnAllBooks() {
        Map<Book, Integer> books = new LinkedHashMap<>();
        books.put(new Book("Book 1", "Author 1", "b-1"), 2);
        books.put(new Book("Book 2", "Author 2", "b-2"), 1);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("b-1", response.getBody().get(0).getId());
        assertEquals(1, response.getBody().get(1).getCopies());
        verify(bookService).getAllBooks();
    }

    @Test
    void getBookByIdShouldReturnBookWhenExists() {
        Book book = new Book("DDD", "Eric Evans", "b-1");
        when(bookService.getBookById("b-1")).thenReturn(book);
        when(bookService.getAvailableCopies("b-1")).thenReturn(2);

        ResponseEntity<BookDTO> response = bookController.getBookById("b-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals("DDD", response.getBody().getTitle());
        assertEquals(2, response.getBody().getCopies());
        verify(bookService).getBookById("b-1");
        verify(bookService).getAvailableCopies("b-1");
    }

    @Test
    void getBookByIdShouldPropagateNotAvailableException() {
        when(bookService.getBookById("missing")).thenThrow(new BookNotAvailableException("missing"));

        BookNotAvailableException ex = assertThrows(BookNotAvailableException.class,
                () -> bookController.getBookById("missing"));

        assertEquals("Book with id missing is not available", ex.getMessage());
        verify(bookService).getBookById("missing");
    }
}
