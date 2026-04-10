package edu.eci.dosw.tdd.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanDTO {
    private String id;
    @NotBlank(message = "Book title is required")
    private String bookTitle;
    @NotBlank(message = "User name is required")
    private String userName;
    @NotNull(message = "Loan date is required")
    private LocalDate loanDate;
    @NotNull(message = "Return date is required")
    private LocalDate returnDate;
    @NotBlank(message = "Status is required")
    private String status;

    public LoanDTO() {
    }

    public LoanDTO(String id, String userName, String bookTitle, LocalDate loanDate, LocalDate returnDate, String status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}
