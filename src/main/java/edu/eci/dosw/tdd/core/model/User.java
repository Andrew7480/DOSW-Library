package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class User {
    private final String id;
    private final String name;
    private final Role role;
    
    public User(String id, String name){
        this.id = id;
        this.name = name;
        this.role = Role.USER;
    }

    public User(String id, String name, Role role){
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
