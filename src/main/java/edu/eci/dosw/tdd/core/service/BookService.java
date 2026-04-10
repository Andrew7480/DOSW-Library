package edu.eci.dosw.tdd.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.eci.dosw.tdd.core.exception.BookAlreadyExitsException;
import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.BookNotFoundException;
import edu.eci.dosw.tdd.core.exception.InvalidInputException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.repository.BookRepository;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.BookValidator;
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
        return bookRepository.save(book);
    }

    public Map<Book, Integer> getAllBooksWithCopies(){
        return bookRepository.findAll().stream().collect(HashMap::new, (map, book) -> map.put(book, book.getAvailableStock()), HashMap::putAll);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return bookRepository.findAllByAuthorIgnoreCase(author);
    }

    public List<Book> getAvailableBooks(){
        return bookRepository.findAllByAvailableStockGreaterThan(0);
    }
    
    public boolean isBookAvailable(String title) {
        var book = findBookByTitleOrThrow(title);
        return book.getAvailableStock() > 0;
    }

    public int getAvailableCopies(String title) {
        return findBookByTitleOrThrow(title).getAvailableStock();
    }
    
    public Book getBook(String title) {
        return findBookByTitleOrThrow(title);
    }

    public boolean bookExists(String title) {
        return bookRepository.findByTitleIgnoreCase(title).isPresent();
    }

    public void increaseAvailableCopies(String title, int copies) {
        Book book = findBookByTitleOrThrow(title);
        book.setAvailableStock(book.getAvailableStock() + copies);
        bookRepository.save(book);
    }

    public void decreaseAvailableCopies(String title, int copies) {
        Book book = findBookByTitleOrThrow(title);
        int availableCopies = book.getAvailableStock();
        if (availableCopies < copies) {
            throw new BookNotAvailableException(title);
        }
        book.setAvailableStock(availableCopies - copies);
        bookRepository.save(book);
    }

    public void updateAvailability(String title, int copies) {
        if (copies < 0) {
            throw new InvalidInputException("Available copies cannot be negative");
        }
        Book book = findBookByTitleOrThrow(title);
        book.setAvailableStock(copies);
        bookRepository.save(book);
    }

    private void verifyBookNotDuplicate(String title) {
        String normalizedTitle = title.trim().toLowerCase();
        if (bookRepository.findByTitleIgnoreCase(normalizedTitle).isPresent()) {
            throw new BookAlreadyExitsException(title);
        }
    }

    private Book findBookByTitleOrThrow(String title) {
        return bookRepository.findByTitleIgnoreCase(title)
            .orElseThrow(() -> new BookNotFoundException(title));
    }
}