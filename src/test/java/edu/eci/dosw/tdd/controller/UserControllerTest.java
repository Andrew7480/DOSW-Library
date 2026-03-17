package edu.eci.dosw.tdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void registerUserShouldReturnCreatedUser() {
        User requestModel = new User("u-1", "Ana");
        when(userService.registerUser("Ana")).thenReturn(requestModel);

        ResponseEntity<UserDTO> response = userController.registerUser(new UserDTO( "Ana"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("u-1", response.getBody().getId());
        assertEquals("Ana", response.getBody().getName());
        verify(userService).registerUser("Ana");
    }

    @Test
    void getUsersShouldReturnAllUsers() {
        when(userService.getUsers()).thenReturn(List.of(
                new User("u-1", "Ana"),
                new User("u-2", "Luis")));

        ResponseEntity<List<UserDTO>> response = userController.getUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("u-1", response.getBody().get(0).getId());
        assertEquals("Luis", response.getBody().get(1).getName());
        verify(userService).getUsers();
    }

    @Test
    void getUserByIdShouldReturnUserWhenExists() {
        when(userService.getUserById("u-1")).thenReturn(new User("u-1", "Ana"));

        ResponseEntity<UserDTO> response = userController.getUserById("u-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("u-1", response.getBody().getId());
        assertEquals("Ana", response.getBody().getName());
        verify(userService).getUserById("u-1");
    }

    @Test
    void getUserByIdShouldPropagateNotFoundException() {
        when(userService.getUserById("missing")).thenThrow(new UserNotFoundException("missing"));

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userController.getUserById("missing"));
        assertEquals("User with id missing not found", ex.getMessage());

        verify(userService).getUserById("missing");
    }
}
