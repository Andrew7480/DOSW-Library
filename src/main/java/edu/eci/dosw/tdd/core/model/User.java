package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class User {
    private final String name;
    private final String id;

    public User(String id, String name){
        this.id = id;
        this.name = name;
    }
}
