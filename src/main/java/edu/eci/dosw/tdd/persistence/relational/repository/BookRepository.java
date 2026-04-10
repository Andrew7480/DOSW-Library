package edu.eci.dosw.tdd.persistence.relational.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.relational.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, String> {
	// Buscar libro por título exacto
	Optional<BookEntity> findByTitleIgnoreCase(String title);

	// Buscar libros por autor
	List<BookEntity> findAllByAuthorIgnoreCase(String author);

	// Libros con stock disponible
	List<BookEntity> findAllByAvailableStockGreaterThan(int minStock);

}
