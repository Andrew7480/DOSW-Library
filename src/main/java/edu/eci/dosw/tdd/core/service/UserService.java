package edu.eci.dosw.tdd.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.eci.dosw.tdd.core.model.User;

public class UserService {
    /*
    Se pueden registrar usuarios, obtener todos los usuarios registrados, 
    y obtener un usuario dependiendo su identificacion
 */
    private Map<String,User> users = new HashMap<>();

    public User registerUser(String name, String id){
        return users.put(id, new User(id, name));
    }

    public List<User> getUsers(){
        return new ArrayList<>(users.values());
    }

    public User getUserById(String id){
        return users.get(id); 
    }
}
