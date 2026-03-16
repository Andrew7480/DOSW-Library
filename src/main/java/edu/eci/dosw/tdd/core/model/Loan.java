package edu.eci.dosw.tdd.core.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Loan {
    private final Book book;
    private final User user;
    private final LocalDate loanDate;
    private StatusLoanEnum status;
    private final LocalDate returnDate;

    public Loan(Book book, User user, LocalDate returnDate) {
        this.book = book;
        this.user = user;
        this.loanDate = LocalDate.now();
        this.status = StatusLoanEnum.ACTIVE;
        this.returnDate = returnDate;
    }
}
