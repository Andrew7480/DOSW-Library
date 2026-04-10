package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.BookDocumentMapper;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepository {

    private final edu.eci.dosw.tdd.persistence.nonrelational.repository.MongoBookRepository repository;

    public BookRepositoryMongoImpl(edu.eci.dosw.tdd.persistence.nonrelational.repository.MongoBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookDocumentMapper.toModel(repository.save(BookDocumentMapper.toDocument(book)));
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(BookDocumentMapper::toModel).toList();
    }

    @Override
    public Optional<Book> findByTitleIgnoreCase(String title) {
        return repository.findAll().stream()
                .filter(book -> book.getTitle() != null)
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .map(BookDocumentMapper::toModel);
    }

    @Override
    public List<Book> findAllByAuthorIgnoreCase(String author) {
        return repository.findAll().stream()
                .filter(book -> book.getAuthor() != null)
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .map(BookDocumentMapper::toModel)
                .toList();
    }

    @Override
    public List<Book> findAllByAvailableStockGreaterThan(int minStock) {
        return repository.findAll().stream()
                .filter(book -> book.getAvailability() != null)
                .filter(book -> book.getAvailability().getAvailableStock() > minStock)
                .map(BookDocumentMapper::toModel)
                .toList();
    }
}
