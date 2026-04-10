package edu.eci.dosw.tdd.core.service;

import java.util.List;

import edu.eci.dosw.tdd.core.exception.UserNameAlreadyExistsException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.util.PasswordHashUtil;
import edu.eci.dosw.tdd.core.validator.UserValidator;
import lombok.Data;

import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {
    /*
    Se pueden registrar usuarios, obtener todos los usuarios registrados, 
    y obtener un usuario dependiendo su identificacion
    */
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String username, String passwordHash){
        UserValidator.validateCreateUser(name, username, passwordHash);
        validateUserNameNotDuplicate(username);

        String id = IdGeneratorUtil.generateId();
        User user = new User(id, name, username, PasswordHashUtil.hashPassword(passwordHash));
        return userRepository.save(user);
    }

    public User registerUser(String name, String username, String passwordHash, Role role){
        UserValidator.validateCreateUser(name, username, passwordHash);
        validateUserNameNotDuplicate(username);
        String id = IdGeneratorUtil.generateId();
        User user = new User(id, name, username, PasswordHashUtil.hashPassword(passwordHash), role);
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private void validateUserNameNotDuplicate(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserNameAlreadyExistsException(username);
        }
    }
}
