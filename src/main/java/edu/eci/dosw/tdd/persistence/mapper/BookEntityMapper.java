package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;

public class BookEntityMapper {

    private BookEntityMapper() {
    }

    public static Book toModel(BookEntity entity) {
        return new Book(entity.getTitle(), entity.getAuthor(), entity.getId());
    }

    public static BookEntity toEntity(Book book, int totalStock, int availableStock) {
        BookEntity entity = new BookEntity();
        entity.setId(book.getId());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setTotalStock(totalStock);
        entity.setAvailableStock(availableStock);
        return entity;
    }
}
