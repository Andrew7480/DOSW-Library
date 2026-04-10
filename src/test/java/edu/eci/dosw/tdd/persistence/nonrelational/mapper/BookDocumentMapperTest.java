package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;

class BookDocumentMapperTest {

    @Test
    void toDocumentShouldMapAvailabilityFields() {
        Book book = new Book("b-1", "Clean Code", "Robert C. Martin", 5, 3);

        BookDocument document = BookDocumentMapper.toDocument(book);

        assertEquals("b-1", document.getId());
        assertEquals("Clean Code", document.getTitle());
        assertEquals("Robert C. Martin", document.getAuthor());
        assertEquals(5, document.getAvailability().getTotalStock());
        assertEquals(3, document.getAvailability().getAvailableStock());
        assertEquals(2, document.getAvailability().getLoanedStock());
        assertEquals("AVAILABLE", document.getAvailability().getStatus());
    }

    @Test
    void toDocumentShouldMarkUnavailableWhenNoStock() {
        Book book = new Book("b-2", "DDD", "Eric Evans", 2, 0);

        BookDocument document = BookDocumentMapper.toDocument(book);

        assertEquals("UNAVAILABLE", document.getAvailability().getStatus());
        assertEquals(2, document.getAvailability().getLoanedStock());
    }

    @Test
    void toModelShouldMapFieldsWhenAvailabilityIsPresent() {
        BookDocument document = new BookDocument();
        document.setId("b-3");
        document.setTitle("Refactoring");
        document.setAuthor("Martin Fowler");

        var availability = new edu.eci.dosw.tdd.persistence.nonrelational.document.AvailabilityDocument();
        availability.setTotalStock(4);
        availability.setAvailableStock(1);
        document.setAvailability(availability);

        Book model = BookDocumentMapper.toModel(document);

        assertEquals("b-3", model.getId());
        assertEquals("Refactoring", model.getTitle());
        assertEquals("Martin Fowler", model.getAuthor());
        assertEquals(4, model.getTotalStock());
        assertEquals(1, model.getAvailableStock());
    }

    @Test
    void toModelShouldDefaultStocksToZeroWhenAvailabilityIsNull() {
        BookDocument document = new BookDocument();
        document.setId("b-4");
        document.setTitle("No Availability");
        document.setAuthor("Unknown");

        Book model = BookDocumentMapper.toModel(document);

        assertEquals(0, model.getTotalStock());
        assertEquals(0, model.getAvailableStock());
    }
}
