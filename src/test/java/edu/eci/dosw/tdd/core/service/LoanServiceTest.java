package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.dosw.tdd.persistence.repository.LoanRepository;


@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 21);
    private static final LocalDate WEEK_LATER = TODAY.plusDays(7);
    private static final LocalDate EIGHT_DAYS_LATER = TODAY.plusDays(8);
    private static final LocalDate TWO_DAYS_LATER = TODAY.plusDays(2);
    private static final LocalDate THREE_DAYS_LATER = TODAY.plusDays(3);
    private static final LocalDate FOUR_DAYS_LATER = TODAY.plusDays(4);
    private static final LocalDate FIVE_DAYS_LATER = TODAY.plusDays(5);

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private LoanRepository loanRepository;

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanService = new LoanService(loanRepository, bookService, userService);
    }

    @Test
    void loanBookShouldCreateActiveLoanWhenDataIsValid() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        LocalDate returnDate = WEEK_LATER;
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan created = loanService.loanBook(userName, bookTitle, returnDate);

        assertNotNull(created);
        assertEquals(bookTitle, created.getBook().getTitle());
        assertEquals(userName, created.getUser().getUsername());
        assertEquals(StatusLoanEnum.ACTIVE, created.getStatus());
        assertEquals(returnDate, created.getReturnDate());

        when(loanRepository.findAll()).thenReturn(List.of(LoanEntityMapper.toEntity(created)));
        assertEquals(1, loanService.getAllLoans().size());

        verify(userService).getUserByUsername(userName);
        verify(bookService).getBook(bookTitle);
        verify(bookService).decreaseAvailableCopies(bookTitle, 1);
    }

    @Test
    void getAllLoansShouldReturnAllCreatedLoans() {
        String userName = "Luis";
        String bookTitle = "Refactoring";
        Book book = new Book("b-1", bookTitle, "Martin Fowler", 3);
        User user = new User("u-1", "luis", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan loan1 = loanService.loanBook(userName, bookTitle, THREE_DAYS_LATER);
        Loan loan2 = loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER);
        when(loanRepository.findAll()).thenReturn(List.of(
            LoanEntityMapper.toEntity(loan1),
            LoanEntityMapper.toEntity(loan2)));
        List<Loan> allLoans = loanService.getAllLoans();
        assertEquals(2, allLoans.size());
    }

    @Test
    void loanBookShouldThrowWhenUserExceedsLoanLimit() {
        String userName = "Sofia";
        String bookTitle = "DDD";
        Book book = new Book("b-1", bookTitle, "Eric Evans", 3);
        User user = new User("u-1", "sofia", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        // Simula que ya existen 3 préstamos activos para el usuario
        Loan l1 = new Loan("l1", user, book, TWO_DAYS_LATER);
        l1.setStatus(StatusLoanEnum.ACTIVE);
        Loan l2 = new Loan("l2", user, book, THREE_DAYS_LATER);
        l2.setStatus(StatusLoanEnum.ACTIVE);
        Loan l3 = new Loan("l3", user, book, FOUR_DAYS_LATER);
        l3.setStatus(StatusLoanEnum.ACTIVE);
        // Simula que el usuario tiene 3 préstamos activos (LoanService cuenta los activos usando findLoansByUsername().stream())
        LoanEntity e1 = LoanEntityMapper.toEntity(l1);
        LoanEntity e2 = LoanEntityMapper.toEntity(l2);
        LoanEntity e3 = LoanEntityMapper.toEntity(l3);
        e1.setStatus(StatusLoanEnum.ACTIVE);
        e2.setStatus(StatusLoanEnum.ACTIVE);
        e3.setStatus(StatusLoanEnum.ACTIVE);
        when(loanRepository.findByUser_Username(userName)).thenReturn(List.of(e1, e2, e3));
        assertThrows(LoanLimitExceededException.class,
            () -> loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER));
    }

    @Test
    void getLoansByUserNameShouldReturnOnlyLoansForThatUser() {
        String userName1 = "Ana";
        String userName2 = "Luis";
        String bookTitle = "Clean Code";
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user1 = new User("u-1", "ana", userName1, "hash");
        User user2 = new User("u-2", "luis", userName2, "hash");

        when(userService.getUserByUsername(userName1)).thenReturn(user1);
        when(userService.getUserByUsername(userName2)).thenReturn(user2);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan l1 = loanService.loanBook(userName1, bookTitle, WEEK_LATER);
        Loan l2 = loanService.loanBook(userName2, bookTitle, EIGHT_DAYS_LATER);
        when(loanRepository.findByUser_Username(userName1)).thenReturn(List.of(LoanEntityMapper.toEntity(l1)));
        List<Loan> loansAna = loanService.getLoansByUsername(userName1);
        assertEquals(1, loansAna.size());
        assertEquals(userName1, loansAna.get(0).getUser().getUsername());
        
    }

    @Test
    void returnBookShouldChangeStatusToReturned() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        LocalDate returnDate = WEEK_LATER;
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan createdLoan = loanService.loanBook(userName, bookTitle, returnDate);

        LoanEntity entity = LoanEntityMapper.toEntity(createdLoan);
        entity.setStatus(StatusLoanEnum.RETURNED);
        createdLoan.setStatus(StatusLoanEnum.RETURNED);
        when(loanRepository.findByUser_UsernameAndBook_Title(userName, bookTitle)).thenReturn(Optional.of(entity));
        loanService.returnBook(userName, bookTitle);
        assertEquals(StatusLoanEnum.RETURNED, createdLoan.getStatus());
        verify(bookService).increaseAvailableCopies(bookTitle, 1);
    }

    @Test
    void returnBookShouldThrowWhenLoanNotFound() {
        String userName = "Ana";
        String bookTitle = "NonExistent";
        Book book = new Book("b-1", bookTitle, "Unknown Author", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.findByUser_UsernameAndBook_Title(userName, bookTitle)).thenReturn(Optional.empty());
        assertThrows(LoanNotFoundException.class,
                () -> loanService.returnBook(userName, bookTitle));
    }

    @Test
    void userHasActiveLoanShouldReturnTrueWhenUserHasActiveLoans() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan l = loanService.loanBook(userName, bookTitle, WEEK_LATER);
        when(loanRepository.findByUser_Username(userName)).thenReturn(List.of(LoanEntityMapper.toEntity(l)));
        boolean hasActive = loanService.userHasActiveLoan(userName);
        assertTrue(hasActive);
    }

    @Test
    void userHasActiveLoanShouldReturnFalseWhenUserHasNoActiveLoans() {
        String userName = "NoLoans";
        User user = new User("u-1", "no loans", userName, "hash");
        
        when(loanRepository.findByUser_Username(userName)).thenReturn(List.of());
        boolean hasActive = loanService.userHasActiveLoan(userName);
        assertFalse(hasActive);
    }

    @Test
    void getActiveLoansShouldReturnOnlyActiveLoansCrossAllUsers() {
        String userName1 = "Ana";
        String userName2 = "Luis";
        String bookTitle = "Clean Code";
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user1 = new User("u-1", "ana", userName1, "hash");
        User user2 = new User("u-2", "luis", userName2, "hash");

        when(userService.getUserByUsername(userName1)).thenReturn(user1);
        when(userService.getUserByUsername(userName2)).thenReturn(user2);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan l1 = loanService.loanBook(userName1, bookTitle, WEEK_LATER);
        Loan l2 = loanService.loanBook(userName2, bookTitle, EIGHT_DAYS_LATER);
        l2.setStatus(StatusLoanEnum.RETURNED);
        when(loanRepository.findAll()).thenReturn(List.of(
            LoanEntityMapper.toEntity(l1),
            LoanEntityMapper.toEntity(l2)));
        List<Loan> activeLoans = loanService.getActiveLoans();
        assertEquals(1, activeLoans.size());
        assertEquals(StatusLoanEnum.ACTIVE, activeLoans.get(0).getStatus());
    }

    @Test
    void getReturnedLoansShouldReturnOnlyReturnedLoans() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan loan = loanService.loanBook(userName, bookTitle, WEEK_LATER);
        loan.setStatus(StatusLoanEnum.RETURNED);
        when(loanRepository.findAll()).thenReturn(List.of(LoanEntityMapper.toEntity(loan)));
        List<Loan> returnedLoans = loanService.getReturnedLoans();
        assertEquals(1, returnedLoans.size());
        assertEquals(StatusLoanEnum.RETURNED, returnedLoans.get(0).getStatus());
    }

    @Test
    void getActiveLoansByUserNameShouldReturnOnlyActiveLoansByUser() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book("b-1", bookTitle, "Robert C. Martin", 3);
        User user = new User("u-1", "ana", userName, "hash");
        
        when(userService.getUserByUsername(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        Loan loan = loanService.loanBook(userName, bookTitle, WEEK_LATER);
        loan.setStatus(StatusLoanEnum.RETURNED);
        Loan loan2 = loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER);
        when(loanRepository.findAll()).thenReturn(List.of(
            LoanEntityMapper.toEntity(loan),
            LoanEntityMapper.toEntity(loan2)));
        List<Loan> activeLoans = loanService.getActiveLoans().stream()
            .filter(l -> l.getUser().getUsername().equals(userName))
            .toList();
        assertEquals(1, activeLoans.size());
        assertEquals(StatusLoanEnum.ACTIVE, activeLoans.get(0).getStatus());
        assertEquals(userName, activeLoans.get(0).getUser().getUsername());
    }
}
