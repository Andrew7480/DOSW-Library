package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends LibraryException {
    public AuthenticationException(String username) {
        super("Credenciales inválidas para el usuario: " + username, HttpStatus.UNAUTHORIZED);
    }
}
