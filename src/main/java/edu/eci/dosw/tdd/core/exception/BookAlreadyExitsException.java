package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class BookAlreadyExitsException extends LibraryException {
    public BookAlreadyExitsException(String title) {
        super("A book with the title '" + title + "' already exists.", HttpStatus.CONFLICT);
    }

}
