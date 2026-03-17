package edu.eci.dosw.tdd.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanDTO {

    @NotBlank
	private String bookId;
    @NotBlank
	private String userId;
	private LocalDate loanDate;
    @NotNull
	private LocalDate returnDate;
	private String status;

    public LoanDTO(String bookId, String userId, LocalDate loanDate, LocalDate returnDate, String status) {
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}
