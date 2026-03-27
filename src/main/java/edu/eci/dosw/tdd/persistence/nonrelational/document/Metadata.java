package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.Data;

@Data
public class Metadata{
    private int pages;
    private String language;
    private String publisher;

}