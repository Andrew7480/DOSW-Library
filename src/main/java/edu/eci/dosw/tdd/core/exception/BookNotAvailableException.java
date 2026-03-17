package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class BookNotAvailableException extends LibraryException {

    public BookNotAvailableException(String bookId) {
        super("Book with id " + bookId + " is not available", HttpStatus.CONFLICT);
    }

}
