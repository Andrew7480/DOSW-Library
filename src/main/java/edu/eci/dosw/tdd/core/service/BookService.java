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
import lombok.Data;

import org.springframework.stereotype.Service;

@Service
@Data
public class BookService {

    /*
    Se pueden agregar libros, un usuario puede obtener todos los libros, obtener un libro por su codigo de identificacion, 
    y se puede actualizar si un libro esta disponible o no
    */
    private Map<Book, Integer> books = new HashMap<>();


    public Book addBook(String title, String author, int copies){
        BookValidator.validateCreateBook(title, author, copies);
        verifyBookNotDuplicate(title);

        Book book = new Book(title, author, IdGeneratorUtil.generateId());
        books.put(book, copies);
        return book;
    }

    public Map<Book, Integer> getAllBooksWithCopies(){
        return new HashMap<>(books);
    }

    public List<Book> getAllBooks(){
        return books.keySet().stream().toList();
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return books.keySet().stream().filter(b -> b.getAuthor().equals(author)).toList();
    }

    public List<Book> getAvailableBooks(){
        return books.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).toList();
    }
    
    public boolean isBookAvailable(String title){
        getBook(title);
        return books.keySet().stream().filter(b -> b.getTitle().equals(title)).findFirst().map(b -> books.get(b) > 0).orElseThrow(() -> new BookNotFoundException(title));
    }

    public int getAvailableCopies(String title) {
        isBookAvailable(title);
        Book book = getBook(title);
        return books.get(book);
    }
    
    public Book getBook(String title) {
        return books.keySet().stream()
                .filter(b -> b.getTitle().equals(title))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(title));
    }

    public boolean bookExists(String title) {
        return books.keySet().stream().anyMatch(b -> b.getTitle().equals(title));
    }

    public void increaseAvailableCopies(String title, int copies) {
        Book book = getBook(title);
        int availableCopies = books.get(book);
        books.put(book, availableCopies + copies);
    }

    public void decreaseAvailableCopies(String title, int copies) {
        isBookAvailable(title);
        Book book = getBook(title);
        int availableCopies = books.get(book);
        if (availableCopies < copies) {
            throw new BookNotAvailableException(title);
        }
        books.put(book, availableCopies - copies);
    }

    
    private void verifyBookNotDuplicate(String title) {
        String normalizedTitle = title.trim().toLowerCase();
        if (books.keySet().stream().anyMatch(b -> b.getTitle().trim().toLowerCase().equals(normalizedTitle))) {
            throw new BookAlreadyExitsException(title);
        }
    }

    public void updateAvailability(String title, int copies) {
        if (copies < 0) {
            throw new InvalidInputException("Available copies cannot be negative");
        }
        Book book = getBook(title);
        books.put(book, copies);
    }
    
}