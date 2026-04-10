package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class LoanNotFoundException extends LibraryException {
    public LoanNotFoundException(String userName, String bookTitle) {
        super(String.format("No active loan found for user '%s' and book '%s'", userName, bookTitle), HttpStatus.NOT_FOUND);
    }

}
