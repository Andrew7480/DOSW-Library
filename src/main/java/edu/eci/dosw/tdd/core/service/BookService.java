package edu.eci.dosw.tdd.core.service;

import java.util.HashMap;
import java.util.Map;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
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
        Book book = new Book(title, author, IdGeneratorUtil.generateId());
        books.put(book, copies);
        return book;
    }

    public Map<Book, Integer> getAllBooks(){
        return new HashMap<>(books);
    }

    public Book getBookById(String id){
        BookValidator.validateBookId(id);
        Book book = findBookById(id);
        if (book == null) {
            throw new BookNotAvailableException(id);
        }
        if (!isBookAvailable(id)) {
            throw new BookNotAvailableException(id);
        }
        return book;
    }
    
    public boolean isBookAvailable(String id){
        BookValidator.validateBookId(id);
        return books.keySet().stream().filter(b -> b.getId().equals(id)).findFirst().map(b -> books.get(b) > 0).orElse(false);
    }

    public int getAvailableCopies(String id) {
        BookValidator.validateBookId(id);
        Book book = findBookById(id);
        if (book == null) {
            return 0;
        }
        return books.get(book);
    }

    public void updateAvailability(String id, int copies) {
        BookValidator.validateBookId(id);
        BookValidator.validateAvailableCopies(copies);
        Book book = findBookById(id);
        if (book == null) {
            throw new BookNotAvailableException(id);
        }
        books.put(book, copies);
    }

    private Book findBookById(String id) {
        return books.keySet().stream().filter(b -> b.getId().equals(id)).findFirst().orElse(null);
    }
    
}
