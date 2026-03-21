package edu.eci.dosw.tdd.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanDTO {

    @NotBlank
	private String bookTitle;
    @NotBlank
	private String userName;
	private LocalDate loanDate;
    @NotNull
	private LocalDate returnDate;
	private String status;

    public LoanDTO(String bookTitle, String userName, LocalDate loanDate, LocalDate returnDate, String status) {
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}
