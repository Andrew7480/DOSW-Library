package edu.eci.dosw.tdd.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.eci.dosw.tdd.core.exception.BookAlreadyExitsException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.InvalidInputException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
import edu.eci.dosw.tdd.persistence.entity.BookEntity;
import edu.eci.dosw.tdd.persistence.mapper.BookEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.BookRepository;
import lombok.Data;

import org.springframework.stereotype.Service;

@Service
@Data
public class BookService {

    /*
    Se pueden agregar libros, un usuario puede obtener todos los libros, obtener un libro por su codigo de identificacion, 
    y se puede actualizar si un libro esta disponible o no
    */
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public Book addBook(String title, String author, int copies){
        BookValidator.validateCreateBook(title, author, copies);
        verifyBookNotDuplicate(title);
        Book book = new Book(IdGeneratorUtil.generateId(), title, author, copies, copies);
        bookRepository.save(BookEntityMapper.toEntity(book));
        return book;
    }

    public Map<Book, Integer> getAllBooksWithCopies(){
        return bookRepository.findAll().stream().collect(HashMap::new, (map, entity) -> map.put(BookEntityMapper.toModel(entity), entity.getAvailableStock()), HashMap::putAll);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll().stream().map(BookEntityMapper::toModel).toList();
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return bookRepository.findAllByAuthorIgnoreCase(author).stream().map(BookEntityMapper::toModel).toList();
    }

    public List<Book> getAvailableBooks(){
        return bookRepository.findAllByAvailableStockGreaterThan(0).stream().map(BookEntityMapper::toModel).toList();
    }
    
    public boolean isBookAvailable(String title) {
        var entity = findBookEntityByTitleOrThrow(title);
        return entity.getAvailableStock() > 0;
    }

    public int getAvailableCopies(String title) {
        return findBookEntityByTitleOrThrow(title).getAvailableStock();
    }
    
    public Book getBook(String title) {
        return BookEntityMapper.toModel(findBookEntityByTitleOrThrow(title));
    }

    public boolean bookExists(String title) {
        return bookRepository.findByTitleIgnoreCase(title).isPresent();
    }

    public void increaseAvailableCopies(String title, int copies) {
        BookEntity entity = findBookEntityByTitleOrThrow(title);
        entity.setAvailableStock(entity.getAvailableStock() + copies);
        bookRepository.save(entity);
    }

    public void decreaseAvailableCopies(String title, int copies) {
        BookEntity entity = findBookEntityByTitleOrThrow(title);
        int availableCopies = entity.getAvailableStock();
        if (availableCopies < copies) {
            throw new BookNotAvailableException(title);
        }
        entity.setAvailableStock(availableCopies - copies);
        bookRepository.save(entity);
    }

    public void updateAvailability(String title, int copies) {
        if (copies < 0) {
            throw new InvalidInputException("Available copies cannot be negative");
        }
        BookEntity entity = findBookEntityByTitleOrThrow(title);
        entity.setAvailableStock(copies);
        bookRepository.save(entity);
    }

    private void verifyBookNotDuplicate(String title) {
        String normalizedTitle = title.trim().toLowerCase();
        if (bookRepository.findByTitleIgnoreCase(normalizedTitle).isPresent()) {
            throw new BookAlreadyExitsException(title);
        }
    }

    private BookEntity findBookEntityByTitleOrThrow(String title) {
        return bookRepository.findByTitleIgnoreCase(title)
            .orElseThrow(() -> new BookNotFoundException(title));
    }
}