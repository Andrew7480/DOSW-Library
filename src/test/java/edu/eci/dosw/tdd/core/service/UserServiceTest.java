package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void registerUserShouldCreateUserWithGeneratedId() {
        User created = userService.registerUser("Ana");

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Ana", created.getName());
    }

    @Test
    void getUsersShouldReturnAllRegisteredUsers() {
        userService.registerUser("Ana");
        userService.registerUser("Luis");

        assertEquals(2, userService.getUsers().size());
    }

    @Test
    void getUserByIdShouldReturnExistingUser() {
        User created = userService.registerUser("Sofia");

        User found = userService.getUserById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("Sofia", found.getName());
    }

    @Test
    void getUserByIdShouldThrowWhenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("missing-id"));
    }

    @Test
    void registerUserShouldThrowWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser("   "));
    }
}
