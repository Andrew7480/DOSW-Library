package edu.eci.dosw.tdd.core.service;

import java.time.LocalDate;
import java.util.List;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.validator.LoanValidator;
import edu.eci.dosw.tdd.persistence.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import lombok.Data;

import org.springframework.stereotype.Service;

@Service
@Data
public class LoanService{

    private static final int MAX_ACTIVE_LOANS_PER_USER = 3;

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }
    
    public Loan loanBook(String userName, String booktitle, LocalDate returnDate) {
        User user = userService.getUserByUsername(userName);
        Book book = bookService.getBook(booktitle);
        LoanValidator.validateLoanRequest(user.getId(), book.getId(), returnDate);
        LoanValidator.validateLoanDates(LocalDate.now(), returnDate);
        canLoanBook(user, book);
        bookService.decreaseAvailableCopies(booktitle, 1);
        Loan loan = new Loan(IdGeneratorUtil.generateId(), user, book, returnDate);
        loanRepository.save(LoanEntityMapper.toEntity(loan));
        return loan;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream().map(LoanEntityMapper::toModel).toList();
    }

    public List<Loan> getLoansByUsername(String userName) {
        return loanRepository.findByUser_Username(userName).stream().map(LoanEntityMapper::toModel).toList();
    }

    public boolean userHasActiveLoan(String userName) {
        return loanRepository.findByUser_Username(userName).stream()
                .map(LoanEntityMapper::toModel)
                .anyMatch(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE);
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.findAll().stream()
                .map(LoanEntityMapper::toModel)
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .toList();
    }

    public List<Loan> getReturnedLoans() {
        return loanRepository.findAll().stream()
                .map(LoanEntityMapper::toModel)
                .filter(loan -> loan.getStatus() == StatusLoanEnum.RETURNED)
                .toList();
    }

    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loanRepository.findAll().stream()
                .map(LoanEntityMapper::toModel)
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .filter(loan -> loan.getReturnDate().isBefore(today))
                .toList();
    }

    public List<Loan> getActiveLoansByUserName(String userName) {
        return loanRepository.findByUser_Username(userName).stream()
                .map(LoanEntityMapper::toModel)
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .toList();
    }

    public void returnBook(String userName, String booktitle) {
        User user = userService.getUserByUsername(userName);
        Book book = bookService.getBook(booktitle);
        LoanValidator.validateLoanRequest(user.getId(), book.getId(), LocalDate.now());

        Loan loan = loanRepository.findByUser_UsernameAndBook_Title(userName, booktitle)
                .map(LoanEntityMapper::toModel)
                .orElseThrow(() -> new LoanNotFoundException(userName, booktitle));

        loan.setStatus(StatusLoanEnum.RETURNED);
        loanRepository.save(LoanEntityMapper.toEntity(loan));
        bookService.increaseAvailableCopies(booktitle, 1);
    }

    private void canLoanBook(User user, Book book) {

        bookService.isBookAvailable(book.getTitle());

        long activeLoans = loanRepository.findByUser_Username(user.getUsername()).stream()
                .map(LoanEntityMapper::toModel)
                .filter(loan -> loan.getStatus() == StatusLoanEnum.ACTIVE)
                .count();

        if (activeLoans >= MAX_ACTIVE_LOANS_PER_USER) {
            throw new LoanLimitExceededException();
        }

    }
}
