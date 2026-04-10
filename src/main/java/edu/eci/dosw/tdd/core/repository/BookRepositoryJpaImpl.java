package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.relational.mapper.BookEntityMapper;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepository {

    private final edu.eci.dosw.tdd.persistence.relational.repository.BookRepository repository;

    public BookRepositoryJpaImpl(edu.eci.dosw.tdd.persistence.relational.repository.BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookEntityMapper.toModel(repository.save(BookEntityMapper.toEntity(book)));
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(BookEntityMapper::toModel).toList();
    }

    @Override
    public Optional<Book> findByTitleIgnoreCase(String title) {
        return repository.findByTitleIgnoreCase(title).map(BookEntityMapper::toModel);
    }

    @Override
    public List<Book> findAllByAuthorIgnoreCase(String author) {
        return repository.findAllByAuthorIgnoreCase(author).stream().map(BookEntityMapper::toModel).toList();
    }

    @Override
    public List<Book> findAllByAvailableStockGreaterThan(int minStock) {
        return repository.findAllByAvailableStockGreaterThan(minStock).stream().map(BookEntityMapper::toModel).toList();
    }
}
