package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;

import java.util.List;

public interface BookRepository extends MongoRepository<BookDocument, String> {
    @Query("{title: '?0'}")
    BookDocument findBookByTitle(String title);

    @Query(value="{author: '?0'}", fields="{title: 1, author: 1}")
    List<BookDocument> findAllByAuthor(String author);
}
