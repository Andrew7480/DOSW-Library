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

@RestController
@Data
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;



	@PostMapping("/add")
	public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO request) {
		Book created = bookService.addBook(request.getTitle(), request.getAuthor(), request.getCopies());
		return new ResponseEntity<>(BookMapper.toDTO(created, request.getCopies()), HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		List<BookDTO> response = bookService.getAllBooks().entrySet().stream()
				.map(entry -> new BookDTO(entry.getKey().getId(), entry.getKey().getTitle(), entry.getKey().getAuthor(), entry.getValue()))
			.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
		return ResponseEntity.ok(BookMapper.toDTO(bookService.getBookById(id), bookService.getAvailableCopies(id)));
	}

	@PatchMapping("/{id}/availability/{copies}")
	public ResponseEntity<Void> updateAvailability(@PathVariable String id, @PathVariable int copies) {
		bookService.updateAvailability(id, copies);
		return ResponseEntity.noContent().build();
	}

}
