package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;

import edu.eci.dosw.tdd.core.exception.InvalidInputException;
import edu.eci.dosw.tdd.core.exception.UserNameAlreadyExistsException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.model.Role;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        // setUP
    }
    
    @Test
    void registerUserShouldCreateUserWithGeneratedId() {
        String name = "Ana";
        String username = "ana";
        String passwordHash = "hash";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.registerUser(name, username, passwordHash);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Ana", created.getName());
        assertEquals("ana", created.getUsername());
    }

    @Test
    void getUsersShouldReturnAllRegisteredUsers() {
        User user1 = new User("id1", "Ana", "ana", "hash1", Role.USER);
        User user2 = new User("id2", "Luis", "luis", "hash2", Role.USER);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getUsers();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Ana")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Luis")));
    }

    @Test
    void getUserByIdShouldReturnExistingUser() {
        User user = new User("id3", "Sofia", "sofia", "hash3", Role.USER);
        when(userRepository.findById("id3")).thenReturn(Optional.of(user));

        User found = userService.getUserById("id3");
        assertEquals("id3", found.getId());
        assertEquals("Sofia", found.getName());
        assertEquals("sofia", found.getUsername());
    }

    @Test
    void getUserByIdShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findById("missing-id")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("missing-id"));
    }

    @Test
    void registerUserShouldThrowWhenNameIsBlank() {
        String blankName = "   ";
        String username = "user";
        String passwordHash = "hash";
        assertThrows(InvalidInputException.class, () -> userService.registerUser(blankName, username, passwordHash));
    }

    @Test
    void registerUserShouldThrowWhenNameIsDuplicate() {
        String name = "Ana";
        String username = "ana";
        String passwordHash = "hash";
        User user = new User("id1", name, username, passwordHash, Role.USER);
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.empty()) 
            .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.registerUser(name, username, passwordHash);
        assertThrows(UserNameAlreadyExistsException.class, () -> userService.registerUser(name, username, passwordHash));
    }

    @Test
    void getUserByUsernameShouldReturnExistingUser() {
        User user = new User("id4", "Maria", "maria", "hash4", Role.USER);
        when(userRepository.findByUsername("maria")).thenReturn(Optional.of(user));
        User found = userService.getUserByUsername("maria");
        assertEquals("id4", found.getId());
        assertEquals("Maria", found.getName());
        assertEquals("maria", found.getUsername());
    }

    @Test
    void getUserByUsernameShouldThrowWhenUserDoesNotExist() {
        when(userRepository.findByUsername("NonExistent")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("NonExistent"));
    }

}
