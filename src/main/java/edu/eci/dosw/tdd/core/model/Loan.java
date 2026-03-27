package edu.eci.dosw.tdd.core.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Loan {
    private final String id;
    private final User user;
    private final Book book;
    private final LocalDate loanDate;
    private final LocalDate returnDate;
    private StatusLoanEnum status;
    private LocalDateTime returnedAt;
    

    public Loan(String id, User user, Book book, LocalDate returnDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.status = StatusLoanEnum.ACTIVE;
        this.returnDate = returnDate;
    }

    public Loan(String id, User user, Book book, LocalDate returnDate, StatusLoanEnum status) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.status = status;
        this.returnDate = returnDate;
    }

    public Loan(String id, User user, Book book, LocalDate loanDate, LocalDate returnDate, StatusLoanEnum status, LocalDateTime returnedAt) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.loanDate = loanDate;
        this.status = status;
        this.returnDate = returnDate;
        this.returnedAt = returnedAt;

    }
}
