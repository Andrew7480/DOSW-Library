package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.LoanNotFoundException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 21);
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

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanService = new LoanService(bookService, userService);
    }

    @Test
    void loanBookShouldCreateActiveLoanWhenDataIsValid() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        LocalDate returnDate = WEEK_LATER;
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        Loan created = loanService.loanBook(userName, bookTitle, returnDate);

        assertNotNull(created);
        assertEquals(bookTitle, created.getBook().getTitle());
        assertEquals(userName, created.getUser().getName());
        assertEquals(StatusLoanEnum.ACTIVE, created.getStatus());
        assertEquals(returnDate, created.getReturnDate());
        assertEquals(1, loanService.getAllLoans().size());
        
        verify(userService).getUserByName(userName);
        verify(bookService).getBook(bookTitle);
        verify(bookService).decreaseAvailableCopies(bookTitle, 1);
    }

    @Test
    void getAllLoansShouldReturnAllCreatedLoans() {
        String userName = "Luis";
        String bookTitle = "Refactoring";
        Book book = new Book(bookTitle, "Martin Fowler", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        loanService.loanBook(userName, bookTitle, THREE_DAYS_LATER);
        loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER);
        List<Loan> allLoans = loanService.getAllLoans();

        assertEquals(2, allLoans.size());
    }

    @Test
    void loanBookShouldThrowWhenUserExceedsLoanLimit() {
        String userName = "Sofia";
        String bookTitle = "DDD";
        Book book = new Book(bookTitle, "Eric Evans", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        loanService.loanBook(userName, bookTitle, TWO_DAYS_LATER);
        loanService.loanBook(userName, bookTitle, THREE_DAYS_LATER);
        loanService.loanBook(userName, bookTitle, FOUR_DAYS_LATER);

        assertThrows(LoanLimitExceededException.class,
                () -> loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER));
    }

    @Test
    void getLoansByUserNameShouldReturnOnlyLoansForThatUser() {
        String userName1 = "Ana";
        String userName2 = "Luis";
        String bookTitle = "Clean Code";
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user1 = new User("u-1", userName1);
        User user2 = new User("u-2", userName2);
        
        when(userService.getUserByName(userName1)).thenReturn(user1);
        when(userService.getUserByName(userName2)).thenReturn(user2);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        loanService.loanBook(userName1, bookTitle, WEEK_LATER);
        loanService.loanBook(userName2, bookTitle, EIGHT_DAYS_LATER);
        List<Loan> loansAna = loanService.getLoansByUserName(userName1);

        assertEquals(1, loansAna.size());
        assertEquals(userName1, loansAna.get(0).getUser().getName());
        
    }

    @Test
    void returnBookShouldChangeStatusToReturned() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        LocalDate returnDate = WEEK_LATER;
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        
        Loan createdLoan = loanService.loanBook(userName, bookTitle, returnDate);

        loanService.returnBook(userName, bookTitle);

        assertEquals(StatusLoanEnum.RETURNED, createdLoan.getStatus());
        
        verify(bookService).increaseAvailableCopies(bookTitle, 1);
    }

    @Test
    void returnBookShouldThrowWhenLoanNotFound() {
        String userName = "Ana";
        String bookTitle = "NonExistent";
        Book book = new Book(bookTitle, "Unknown Author", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);

        assertThrows(LoanNotFoundException.class,
                () -> loanService.returnBook(userName, bookTitle));
    }

    @Test
    void userHasActiveLoanShouldReturnTrueWhenUserHasActiveLoans() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        
        loanService.loanBook(userName, bookTitle, WEEK_LATER);

        boolean hasActive = loanService.userHasActiveLoan(userName);

        assertTrue(hasActive);
    }

    @Test
    void userHasActiveLoanShouldReturnFalseWhenUserHasNoActiveLoans() {
        String userName = "NoLoans";
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);

        boolean hasActive = loanService.userHasActiveLoan(userName);

        assertFalse(hasActive);
    }

    @Test
    void getActiveLoansShouldReturnOnlyActiveLoansCrossAllUsers() {
        String userName1 = "Ana";
        String userName2 = "Luis";
        String bookTitle = "Clean Code";
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user1 = new User("u-1", userName1);
        User user2 = new User("u-2", userName2);
        
        when(userService.getUserByName(userName1)).thenReturn(user1);
        when(userService.getUserByName(userName2)).thenReturn(user2);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        
        loanService.loanBook(userName1, bookTitle, WEEK_LATER);
        Loan loan2 = loanService.loanBook(userName2, bookTitle, EIGHT_DAYS_LATER);
        loan2.setStatus(StatusLoanEnum.RETURNED);

        List<Loan> activeLoans = loanService.getActiveLoans();

        assertEquals(1, activeLoans.size());
        assertEquals(StatusLoanEnum.ACTIVE, activeLoans.get(0).getStatus());
    }

    @Test
    void getReturnedLoansShouldReturnOnlyReturnedLoans() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        
        Loan loan = loanService.loanBook(userName, bookTitle, WEEK_LATER);
        loan.setStatus(StatusLoanEnum.RETURNED);

        List<Loan> returnedLoans = loanService.getReturnedLoans();

        assertEquals(1, returnedLoans.size());
        assertEquals(StatusLoanEnum.RETURNED, returnedLoans.get(0).getStatus());
    }

    @Test
    void getActiveLoansByUserNameShouldReturnOnlyActiveLoansByUser() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        Book book = new Book(bookTitle, "Robert C. Martin", "b-1");
        User user = new User("u-1", userName);
        
        when(userService.getUserByName(userName)).thenReturn(user);
        when(bookService.getBook(bookTitle)).thenReturn(book);
        
        Loan loan = loanService.loanBook(userName, bookTitle, WEEK_LATER);
        loan.setStatus(StatusLoanEnum.RETURNED);
        loanService.loanBook(userName, bookTitle, FIVE_DAYS_LATER);

        List<Loan> activeLoans = loanService.getActiveLoansByUserName(userName);

        assertEquals(1, activeLoans.size());
        assertEquals(StatusLoanEnum.ACTIVE, activeLoans.get(0).getStatus());
        assertEquals(userName, activeLoans.get(0).getUser().getName());
    }
}
