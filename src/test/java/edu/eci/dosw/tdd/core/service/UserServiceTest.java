package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

import edu.eci.dosw.tdd.core.exception.InvalidInputException;
import edu.eci.dosw.tdd.core.exception.UserNameAlreadyExistsException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService();
    }
    
    @Test
    void registerUserShouldCreateUserWithGeneratedId() {
        String name = "Ana";

        User created = userService.registerUser(name);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Ana", created.getName());
    }

    @Test
    void getUsersShouldReturnAllRegisteredUsers() {
        userService.registerUser("Ana");
        userService.registerUser("Luis");

        int totalUsers = userService.getUsers().size();

        assertEquals(2, totalUsers);
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
        String blankName = "   ";
        
        assertThrows(InvalidInputException.class, () -> userService.registerUser(blankName));
    }

    @Test
    void registerUserShouldThrowWhenNameIsDuplicate() {
        String name = "Ana";
        userService.registerUser(name);
        
        assertThrows(UserNameAlreadyExistsException.class, () -> userService.registerUser(name));
    }

    @Test
    void getUserByNameShouldReturnExistingUser() {
        User created = userService.registerUser("Maria");
        
        User found = userService.getUserByName("Maria");
        
        assertEquals(created.getId(), found.getId());
        assertEquals("Maria", found.getName());
    }

    @Test
    void getUserByNameShouldThrowWhenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByName("NonExistent"));
    }

}
