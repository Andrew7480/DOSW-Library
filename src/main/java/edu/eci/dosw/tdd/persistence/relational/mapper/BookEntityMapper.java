package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;

public class BookEntityMapper {

    private BookEntityMapper() {
    }

	// Book -> BookEntity
	public static BookEntity toEntity(Book book) {
		return new BookEntity(book.getId(), book.getTitle(), book.getAuthor(), book.getTotalStock(), book.getAvailableStock());
	}

	// BookEntity -> Book
	public static Book toModel(BookEntity entity) {
		return new Book(entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getTotalStock(), entity.getAvailableStock());
	}

}
