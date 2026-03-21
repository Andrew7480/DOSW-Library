package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class UserDeletionNotAllowedException  extends LibraryException {

	public UserDeletionNotAllowedException(String id) {
        super("User " + id + " has active loans and cannot be deleted", HttpStatus.CONFLICT);
    }

}