package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class User {
    private String name;
    private String ID;

    public User(String id, String name){
        ID = id;
        this.name = name;
    }
}
