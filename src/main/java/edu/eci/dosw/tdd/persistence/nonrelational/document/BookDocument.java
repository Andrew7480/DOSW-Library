package edu.eci.dosw.tdd.persistence.nonrelational.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.util.List;

@Document(collection = "books")
@Data
public class BookDocument {
    @Id
    private String id;
    private String title;
    private List<String> categories;
    private String postType;
    private String publicationDate;
    private String isbnCode;
    private String author;
    private String catalogDate;
    private Metadata metadata;
    private String language;
    private String publisher;
    private Availability availability;

    
}
