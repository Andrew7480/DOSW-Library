package edu.eci.dosw.tdd.controller;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.service.LoanService;
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
@RequestMapping("/loans")
@Data
public class LoanController {

	private final LoanService loanService;

	@PostMapping("/loan")
	public ResponseEntity<LoanDTO> loanBook(@Valid @RequestBody LoanDTO request) {
		return new ResponseEntity<>(
				LoanMapper.toDTO(loanService.loanBook(request.getUserName(), request.getBookTitle(), request.getReturnDate())),
				HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<LoanDTO>> getLoans() {
		List<LoanDTO> response = loanService.getAllLoans().stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{userName}")
	public ResponseEntity<List<LoanDTO>> getLoansByUsername(@PathVariable String userName) {
		List<LoanDTO> response = loanService.getLoansByUsername(userName).stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/active/{userName}")
	public ResponseEntity<Boolean> userHasActiveLoan(@PathVariable String userName) {
		return ResponseEntity.ok(loanService.userHasActiveLoan(userName));
	}

	@GetMapping("/active")
	public ResponseEntity<List<LoanDTO>> getActiveLoans() {
		List<Loan> activeLoans = loanService.getActiveLoans();
		List<LoanDTO> response = activeLoans.stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/overdue")
	public ResponseEntity<List<LoanDTO>> getOverdueLoans() {
		List<Loan> overdueLoans = loanService.getOverdueLoans();
		List<LoanDTO> response = overdueLoans.stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/returned")
	public ResponseEntity<List<LoanDTO>> getReturnedLoans() {
		List<Loan> returnedLoans = loanService.getReturnedLoans();
		List<LoanDTO> response = returnedLoans.stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/{userName}/active")
	public ResponseEntity<List<LoanDTO>> getActiveLoansByUserName(@PathVariable String userName) {
		List<Loan> activeLoans = loanService.getActiveLoansByUserName(userName);
		List<LoanDTO> response = activeLoans.stream().map(LoanMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/return/{userName}/{bookTitle}")
	public ResponseEntity<Void> returnBook(@PathVariable String userName, @PathVariable String bookTitle) {
		loanService.returnBook(userName, bookTitle);
		return ResponseEntity.ok().build();
	}
	

}
