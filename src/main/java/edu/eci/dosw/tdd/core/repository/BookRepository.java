package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import edu.eci.dosw.tdd.core.model.Book;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findByTitleIgnoreCase(String title);

    List<Book> findAllByAuthorIgnoreCase(String author);

    List<Book> findAllByAvailableStockGreaterThan(int minStock);
}
