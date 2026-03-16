package edu.eci.dosw.tdd.controller;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;
import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans")
@Data
public class LoanController {

	private final LoanService loanService;

	@PostMapping("/loan")
	public ResponseEntity<LoanDTO> loanBook(@Valid @RequestBody LoanDTO request) {
		return new ResponseEntity<>(
				LoanMapper.toDTO(loanService.loanBook(request.getUserId(), request.getBookId(), request.getReturnDate())),
				HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<LoanDTO>> getLoans() {
		List<LoanDTO> response = loanService.getAllLoans().stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

}
