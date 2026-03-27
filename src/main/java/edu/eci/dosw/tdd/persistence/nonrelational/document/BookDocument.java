package edu.eci.dosw.tdd.persistence.nonrelational.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Document(collection = "books")
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
    private Map<String, Object> metadata;
    private String language;
    private String publisher;
    private Availability availability;

    public static class Availability {
        private String status;
        private int totalStock;
        private int availableStock;
        private int loanedStock;
        // getters y setters
    }
    // getters y setters
}
