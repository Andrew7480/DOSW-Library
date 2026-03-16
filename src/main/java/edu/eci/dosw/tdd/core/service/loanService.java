package edu.eci.dosw.tdd.core.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import lombok.Data;

import org.springframework.stereotype.Service;

@Service
@Data
public class LoanService {

    private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

    private final List<Loan> loans = new ArrayList<>();
    private final BookService bookService;
    private final UserService userService;


    public Loan loanBook(String userId, String bookId, LocalDate returnDate) {
        LoanValidator.validateLoanRequest(userId, bookId, returnDate);

        long activeLoans = loans.stream()
                .filter(loan -> loan.getUser().getId().equals(userId))
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .count();

        if (activeLoans >= MAX_ACTIVE_LOANS_PER_USER) {
            throw new LoanLimitExceededException();
        }

        Loan loan = new Loan(bookService.getBookById(bookId), userService.getUserById(userId), returnDate);
        loans.add(loan);
        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }
}
