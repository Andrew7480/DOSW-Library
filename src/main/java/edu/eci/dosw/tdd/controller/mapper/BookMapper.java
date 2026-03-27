package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.core.model.Book;

public class BookMapper {

	private BookMapper() {
	}
	
	public static BookDTO toDTO(Book book) {
		return new BookDTO(
				book.getId(),
				book.getTitle(),
				book.getAuthor(),
				book.getTotalStock(),
				book.getAvailableStock());
	}

	public static Book toModel(BookDTO dto) {
		return new Book(dto.getId(), dto.getTitle(), dto.getAuthor(), dto.getStock(), dto.getAvailableCopies());
	}
}
