package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.eci.dosw.tdd.core.exception.UserNameAlreadyExistsException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.UserDeleteValidator;
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
    private Map<String,User> users = new HashMap<>();

    private final UserDeleteValidator userDeleteValidator;

    public User registerUser(String name){
        UserValidator.validateCreateUser(name);
        validateUserNameNotDuplicate(name);

        String id = IdGeneratorUtil.generateId();
        User user = new User(id, name);
        users.put(id, user);
        return user;
    }

    public List<User> getUsers(){
        return new ArrayList<>(users.values());
    }

    public User getUserById(String id){
        UserValidator.validateUserId(id);
        if (users.get(id) == null) {
            throw new UserNotFoundException(id);
        }
        return users.get(id); 
    }

    public User getUserByName(String name){
        UserValidator.validateUserName(name);
        return users.values().stream().filter(u -> u.getName().equals(name)).findFirst().orElseThrow(() -> new UserNotFoundException(name));
    }


    //voy a trabajar queun usuario se pueda eliminar si no tiene loans activos
    public void deleteUser(String id){
        getUserById(id);
        userDeleteValidator.validateUserCanBeDeleted(id);
        users.remove(id);
    }

    private void validateUserNameNotDuplicate(String name) {
        if (users.values().stream().anyMatch(u -> u.getName().equals(name))) {
            throw new UserNameAlreadyExistsException(name);
        }
    }
}
