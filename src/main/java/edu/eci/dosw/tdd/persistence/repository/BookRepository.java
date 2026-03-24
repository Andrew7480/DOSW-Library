package edu.eci.dosw.tdd.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, String> {

    Optional<BookEntity> findByTitleIgnoreCase(String title);

    List<BookEntity> findByAuthorIgnoreCase(String author);

    List<BookEntity> findByAvailableStockGreaterThan(int minimumStock);

    boolean existsByTitleIgnoreCase(String title);
}
