package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class Book {
    private final String id;
    private final String title;
    private final String author;
    private final int totalStock;
    private int availableStock;
    
    public Book(String id, String title, String author,  int totalStock) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.totalStock = totalStock;
        this.availableStock = totalStock;
    }

    public Book(String id, String title, String author, int totalStock, int availableStock) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }
}
