package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends LibraryException {

	public UserNotFoundException(String userId) {
        super("User with id " + userId + " not found", HttpStatus.NOT_FOUND);
    }

}
