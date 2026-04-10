package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends LibraryException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
