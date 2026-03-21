package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import edu.eci.dosw.tdd.core.exception.InvalidInputException;
import edu.eci.dosw.tdd.core.exception.UserDeletionNotAllowedException;
import edu.eci.dosw.tdd.core.exception.UserNameAlreadyExistsException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.validator.UserDeleteValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    
    @Mock
    private UserDeleteValidator userDeleteValidator;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDeleteValidator);
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

    @Test
    void deleteUserShouldRemoveUserWhenNoActiveLoans() {
        User created = userService.registerUser("Pedro");
        doNothing().when(userDeleteValidator).validateUserCanBeDeleted(created.getId());
        
        userService.deleteUser(created.getId());
        
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(created.getId()));
        
        verify(userDeleteValidator).validateUserCanBeDeleted(created.getId());
    }

    @Test
    void deleteUserShouldThrowWhenUserHasActiveLoans() {
        User created = userService.registerUser("Juan");
        doThrow(new UserDeletionNotAllowedException(created.getId()))
            .when(userDeleteValidator)
            .validateUserCanBeDeleted(created.getId());
        
        assertThrows(UserDeletionNotAllowedException.class, () -> userService.deleteUser(created.getId()));
        
        verify(userDeleteValidator).validateUserCanBeDeleted(created.getId());
    }

    @Test
    void deleteUserShouldThrowWhenUserDoesNotExist() {
        String nonExistentId = "missing-id";  
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(nonExistentId));
    }
}
