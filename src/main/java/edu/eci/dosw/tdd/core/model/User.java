package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class User {
    private final String id;
    private final String name;
    private final String username;
    private final String passwordHash;
    private final Role role;
    
    public User(String id, String name, String username, String passwordHash){
        this.id = id;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = Role.USER;
    }

    public User(String id, String name, String username, String passwordHash, Role role){
        this.id = id;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
