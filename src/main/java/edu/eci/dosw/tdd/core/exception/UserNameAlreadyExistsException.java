package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class UserNameAlreadyExistsException extends LibraryException {
    public UserNameAlreadyExistsException(String name) {
        super("El nombre de usuario '" + name + "' ya existe.", HttpStatus.CONFLICT);
    }

}
