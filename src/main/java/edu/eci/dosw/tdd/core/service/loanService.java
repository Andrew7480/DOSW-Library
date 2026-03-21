package edu.eci.dosw.tdd.core.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
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


    public Loan loanBook(String userName, String booktitle, LocalDate returnDate) {
        User user = userService.getUserByName(userName);
        Book book = bookService.getBook(booktitle);
        LoanValidator.validateLoanRequest(user.getId(), book.getId(), returnDate);

        canLoanBook(user, book);

        bookService.decreaseAvailableCopies(booktitle, 1);

        Loan loan = new Loan(book, user, returnDate);
        loans.add(loan);
        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Loan> getLoansByUserName(String userName) {
        User user = userService.getUserByName(userName);
        return loans.stream()
                .filter(loan -> loan.getUser().getId().equals(user.getId()))
                .toList();
    }

    public void returnBook(String userName, String booktitle) {
        User user = userService.getUserByName(userName);
        Book book = bookService.getBook(booktitle);
        LoanValidator.validateLoanRequest(user.getId(), book.getId(), LocalDate.now());

        Loan loan = loans.stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .filter(l -> l.getBook().getId().equals(book.getId()))
                .filter(l -> l.getStatus() == StatusLoanEnum.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new LoanNotFoundException(user.getName(), book.getTitle()));

        loan.setStatus(StatusLoanEnum.RETURNED);
        bookService.increaseAvailableCopies(booktitle, 1);
    }

    public boolean userHasActiveLoan(String userName) {
        User user = userService.getUserByName(userName);
        return loans.stream()
                .filter(loan -> loan.getUser().getId().equals(user.getId()))
                .anyMatch(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE);
    }

    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .toList();
    }

    public List<Loan> getReturnedLoans() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == StatusLoanEnum.RETURNED)
                .toList();
    }

    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loans.stream()
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .filter(loan -> loan.getReturnDate().isBefore(today))
                .toList();
    }

    public List<Loan> getActiveLoansByUserName(String userName) {
        User user = userService.getUserByName(userName);
        return loans.stream()
                .filter(loan -> loan.getUser().getId().equals(user.getId()))
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .toList();
    }


    private void canLoanBook(User user, Book book) {

        bookService.isBookAvailable(book.getTitle());

        long activeLoans = loans.stream()
                .filter(loan -> loan.getUser().getId().equals(user.getId()))
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .count();

        if (activeLoans >= MAX_ACTIVE_LOANS_PER_USER) {
            throw new LoanLimitExceededException();
        }

    }
}
