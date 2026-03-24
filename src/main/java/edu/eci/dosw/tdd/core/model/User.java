package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class User {
    private final String id;
    private final String name;
    

    public User(String id, String name){
        this.id = id;
        this.name = name;
    }
}
