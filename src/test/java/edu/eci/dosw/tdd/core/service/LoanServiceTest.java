package edu.eci.dosw.tdd.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
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
        Book book = new Book("Clean Code", "Robert C. Martin", "b-1");
        User user = new User("u-1", "Ana");
        LocalDate returnDate = LocalDate.now().plusDays(7);

        when(bookService.getBookById("b-1")).thenReturn(book);
        when(userService.getUserById("u-1")).thenReturn(user);

        Loan created = loanService.loanBook("u-1", "b-1", returnDate);

        assertNotNull(created);
        assertEquals("b-1", created.getBook().getId());
        assertEquals("u-1", created.getUser().getId());
        assertEquals(StatusLoanEnum.ACTIVE, created.getStatus());
        assertEquals(returnDate, created.getReturnDate());
        assertEquals(1, loanService.getAllLoans().size());
    }

    @Test
    void getAllLoansShouldReturnCreatedLoans() {
        Book book = new Book("Refactoring", "Martin Fowler", "b-1");
        User user = new User("u-1", "Luis");
        when(bookService.getBookById("b-1")).thenReturn(book);
        when(userService.getUserById("u-1")).thenReturn(user);

        loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(3));
        loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(5));

        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void loanBookShouldThrowWhenUserExceedsLoanLimit() {
        Book book = new Book("DDD", "Eric Evans", "b-1");
        User user = new User("u-1", "Sofia");
        when(bookService.getBookById("b-1")).thenReturn(book);
        when(userService.getUserById("u-1")).thenReturn(user);

        loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(2));
        loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(3));
        loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(4));

        assertThrows(LoanLimitExceededException.class,
                () -> loanService.loanBook("u-1", "b-1", LocalDate.now().plusDays(5)));
    }

    @Test
    void loanBookShouldThrowWhenReturnDateIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> loanService.loanBook("u-1", "b-1", LocalDate.now().minusDays(1)));
    }
}
