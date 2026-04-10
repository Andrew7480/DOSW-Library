package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.document.AvailabilityDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;

public class BookDocumentMapper {

    private BookDocumentMapper() {
    }

    public static BookDocument toDocument(Book book) {
        BookDocument document = new BookDocument();
        document.setId(book.getId());
        document.setTitle(book.getTitle());
        document.setAuthor(book.getAuthor());

        AvailabilityDocument availability = new AvailabilityDocument();
        availability.setTotalStock(book.getTotalStock());
        availability.setAvailableStock(book.getAvailableStock());
        availability.setLoanedStock(Math.max(0, book.getTotalStock() - book.getAvailableStock()));
        availability.setStatus(book.getAvailableStock() > 0 ? "AVAILABLE" : "UNAVAILABLE");
        document.setAvailability(availability);

        return document;
    }

    public static Book toModel(BookDocument document) {
        int totalStock = 0;
        int availableStock = 0;

        if (document.getAvailability() != null) {
            totalStock = document.getAvailability().getTotalStock();
            availableStock = document.getAvailability().getAvailableStock();
        }

        return new Book(
                document.getId(),
                document.getTitle(),
                document.getAuthor(),
                totalStock,
                availableStock
        );
    }
}
