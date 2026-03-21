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
        Book book1 = new Book("Book 1", "Author 1", "b-1");
        Book book2 = new Book("Book 2", "Author 2", "b-2");
        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));
        when(bookService.getAvailableCopies("Book 1")).thenReturn(2);
        when(bookService.getAvailableCopies("Book 2")).thenReturn(1);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("b-1", response.getBody().get(0).getId());
        assertEquals(1, response.getBody().get(1).getCopies());
        
        verify(bookService).getAllBooks();
        verify(bookService).getAvailableCopies("Book 1");
        verify(bookService).getAvailableCopies("Book 2");
    }

    @Test
    void getBookByTitleShouldReturnBookWhenExists() {
        String title = "DDD";
        Book book = new Book(title, "Eric Evans", "b-1");
        when(bookService.getBook(title)).thenReturn(book);
        when(bookService.getAvailableCopies(title)).thenReturn(2);

        ResponseEntity<BookDTO> response = bookController.getBookByTitle(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals(title, response.getBody().getTitle());
        assertEquals(2, response.getBody().getCopies());
        
        verify(bookService).getBook(title);
        verify(bookService).getAvailableCopies(title);
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
        Book book1 = new Book("Clean Code", author, "b-1");
        Book book2 = new Book("Clean Architecture", author, "b-2");
        when(bookService.getAllBooksByAuthor(author)).thenReturn(List.of(book1, book2));
        when(bookService.getAvailableCopies("Clean Code")).thenReturn(3);
        when(bookService.getAvailableCopies("Clean Architecture")).thenReturn(2);

        ResponseEntity<List<BookDTO>> response = bookController.getBooksByAuthor(author);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        
        verify(bookService).getAllBooksByAuthor(author);
    }

    @Test
    void getAvailableBooksShouldReturnOnlyAvailableBooks() {
        Book book1 = new Book("Available", "Author 1", "b-1");
        when(bookService.getAvailableBooks()).thenReturn(List.of(book1));
        when(bookService.getAvailableCopies("Available")).thenReturn(1);

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
    void updateBookShouldUpdateTitleAuthorAndCopies() {
        String oldTitle = "Old Title";
        Book updatedBook = new Book("New Title", "New Author", "b-1");
        BookDTO requestDTO = new BookDTO("New Title", "New Author", 5);
        when(bookService.getAvailableCopies(oldTitle)).thenReturn(2);
        doNothing().when(bookService).decreaseAvailableCopies(oldTitle, 2);
        when(bookService.addBook("New Title", "New Author", 5)).thenReturn(updatedBook);

        ResponseEntity<BookDTO> response = bookController.updateBook(oldTitle, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getId());
        assertEquals("New Title", response.getBody().getTitle());
        assertEquals("New Author", response.getBody().getAuthor());
        assertEquals(5, response.getBody().getCopies());

        verify(bookService).getAvailableCopies(oldTitle);
        verify(bookService).decreaseAvailableCopies(oldTitle, 2);
        verify(bookService).addBook("New Title", "New Author", 5);
    }
}
