package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String bookId) {
        super("Book with ID '" + bookId + "' not found.", HttpStatus.NOT_FOUND);
    }

}
