package edu.eci.dosw.tdd.controller;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.mapper.BookMapper;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.service.BookService;
import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@Data
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;



	@PostMapping("/add")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO request) {
		Book created = bookService.addBook(request.getTitle(), request.getAuthor(), request.getStock());
		return new ResponseEntity<>(BookMapper.toDTO(created), HttpStatus.CREATED);
	}
	@GetMapping("/allWithCopies")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<List<BookDTO>> getAllBooksWithCopies() {
		List<BookDTO> response = bookService.getAllBooksWithCopies().entrySet().stream()
				.map(e -> new BookDTO(e.getKey().getId(), e.getKey().getTitle(), e.getKey().getAuthor(), e.getKey().getTotalStock(), e.getValue()))
			.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		List<BookDTO> response = bookService.getAllBooks().stream()
				.map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getTotalStock(), book.getAvailableStock()))
			.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/author/{author}")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable String author) {
		List<BookDTO> response = bookService.getAllBooksByAuthor(author).stream()
				.map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getTotalStock(), book.getAvailableStock()))
			.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/available")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<List<BookDTO>> getAvailableBooks() {
		List<BookDTO> response = bookService.getAvailableBooks().stream()
				.map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getTotalStock(), book.getAvailableStock()))
			.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/available/{title}")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<Boolean> isBookAvailable(@PathVariable String title) {
		return ResponseEntity.ok(bookService.isBookAvailable(title));
	}
	@GetMapping("/copies/{title}")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<Integer> getAvailableCopies(@PathVariable String title) {
		return ResponseEntity.ok(bookService.getAvailableCopies(title));
	}

	@GetMapping("/{title}")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
		return ResponseEntity.ok(BookMapper.toDTO(bookService.getBook(title)));
	}

	@GetMapping("/exists/{title}")
	@PreAuthorize("hasAnyRole('USER','LIBRARIAN')")
	public ResponseEntity<Boolean> bookExists(@PathVariable String title) {
		return ResponseEntity.ok(bookService.bookExists(title));
	}

	@PatchMapping("/increaseCopies/{title}/{copies}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<Void> increaseAvailableCopies(@PathVariable String title, @PathVariable int copies) {
		bookService.increaseAvailableCopies(title, copies);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/decreaseCopies/{title}/{copies}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<Void> decreaseAvailableCopies(@PathVariable String title, @PathVariable int copies) {
		bookService.decreaseAvailableCopies(title, copies);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/update/{title}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<Void> updateAvailabilityBook(@PathVariable String title, @RequestBody int copies) {
		bookService.updateAvailability(title, copies);
		return ResponseEntity.ok().build();
	}

}
