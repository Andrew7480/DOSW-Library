package edu.eci.dosw.tdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 3, 21);
    private static final LocalDate TWO_DAYS_LATER = TODAY.plusDays(2);
    private static final LocalDate FOUR_DAYS_LATER = TODAY.plusDays(4);
    private static final LocalDate FIVE_DAYS_LATER = TODAY.plusDays(5);
    private static final LocalDate SEVEN_DAYS_LATER = TODAY.plusDays(7);

    @Mock
    private LoanService loanService;

    private LoanController loanController;

    @BeforeEach
    void setUp() {
        loanController = new LoanController(loanService);
    }

    @Test
    void loanBookShouldReturnCreatedLoan() {
        Loan loan = new Loan(new Book("Clean Code", "Robert C. Martin", "b-1"), new User("u-1", "Ana"),
                SEVEN_DAYS_LATER);
        when(loanService.loanBook("Ana", "Clean Code", loan.getReturnDate())).thenReturn(loan);

        ResponseEntity<LoanDTO> response = loanController
            .loanBook(new LoanDTO("Clean Code", "Ana", null, loan.getReturnDate(), null));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Clean Code", response.getBody().getBookTitle());
        assertEquals("Ana", response.getBody().getUserName());
        assertEquals("ACTIVE", response.getBody().getStatus());
        verify(loanService).loanBook("Ana", "Clean Code", loan.getReturnDate());
    }

    @Test
    void getLoansShouldReturnAllLoans() {
        Loan loan1 = new Loan(new Book("Book 1", "Author 1", "b-1"), new User("u-1", "Ana"),
                TWO_DAYS_LATER);
        Loan loan2 = new Loan(new Book("Book 2", "Author 2", "b-2"), new User("u-2", "Luis"),
                FOUR_DAYS_LATER);
        when(loanService.getAllLoans()).thenReturn(List.of(loan1, loan2));

        ResponseEntity<List<LoanDTO>> response = loanController.getLoans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Book 1", response.getBody().get(0).getBookTitle());
        assertEquals("Luis", response.getBody().get(1).getUserName());
        verify(loanService).getAllLoans();
    }

    @Test
    void loanBookShouldPropagateLoanLimitExceededException() {
        LocalDate returnDate = FIVE_DAYS_LATER;
        when(loanService.loanBook("Ana", "Clean Code", returnDate)).thenThrow(new LoanLimitExceededException());

        LoanDTO requestDTO = new LoanDTO("Clean Code", "Ana", null, returnDate, null);
        LoanLimitExceededException ex = assertThrows(LoanLimitExceededException.class,
                () -> loanController.loanBook(requestDTO));
        assertNotNull(ex.getMessage());
        assertEquals("Loan limit exceeded", ex.getMessage());
        verify(loanService).loanBook("Ana", "Clean Code", returnDate);
    }

    @Test
    void getLoansByUserNameShouldReturnLoansForThatUser() {
        String userName = "Ana";
        Loan loan = new Loan(new Book("Clean Code", "Robert C. Martin", "b-1"), new User("u-1", userName),
                SEVEN_DAYS_LATER);
        when(loanService.getLoansByUserName(userName)).thenReturn(List.of(loan));

        ResponseEntity<List<LoanDTO>> response = loanController.getLoansByUserName(userName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Clean Code", response.getBody().get(0).getBookTitle());
        assertEquals(userName, response.getBody().get(0).getUserName());
        verify(loanService).getLoansByUserName(userName);
    }

    @Test
    void getActiveLoansByUserNameShouldReturnActiveLoansForThatUser() {
        String userName = "Ana";
        Loan loan = new Loan(new Book("Clean Code", "Robert C. Martin", "b-1"), new User("u-1", userName),
                SEVEN_DAYS_LATER);
        when(loanService.getActiveLoansByUserName(userName)).thenReturn(List.of(loan));

        ResponseEntity<List<LoanDTO>> response = loanController.getActiveLoansByUserName(userName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Clean Code", response.getBody().get(0).getBookTitle());
        assertEquals(userName, response.getBody().get(0).getUserName());
        verify(loanService).getActiveLoansByUserName(userName);
    }

    @Test
    void getActiveLoansShouldReturnOnlyActiveLoansCrossAllUsers() {
        Loan loan1 = new Loan(new Book("Clean Code", "Robert C. Martin", "b-1"), new User("u-1", "Ana"),
                SEVEN_DAYS_LATER);
        when(loanService.getActiveLoans()).thenReturn(List.of(loan1));

        ResponseEntity<List<LoanDTO>> response = loanController.getActiveLoans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(loanService).getActiveLoans();
    }

    @Test
    void getReturnedLoansShouldReturnOnlyReturnedLoans() {
        Loan loan1 = new Loan(new Book("Clean Code", "Robert C. Martin", "b-1"), new User("u-1", "Ana"),
                SEVEN_DAYS_LATER);
        loan1.setStatus(StatusLoanEnum.RETURNED);
        when(loanService.getReturnedLoans()).thenReturn(List.of(loan1));

        ResponseEntity<List<LoanDTO>> response = loanController.getReturnedLoans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("RETURNED", response.getBody().get(0).getStatus());
        verify(loanService).getReturnedLoans();
    }

    @Test
    void returnBookShouldDelegateToServiceAndReturnOk() {
        String userName = "Ana";
        String bookTitle = "Clean Code";
        doNothing().when(loanService).returnBook(userName, bookTitle);

        ResponseEntity<Void> response = loanController.returnBook(userName, bookTitle);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(loanService).returnBook(userName, bookTitle);
    }
}
