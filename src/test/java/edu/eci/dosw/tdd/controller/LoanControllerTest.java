package edu.eci.dosw.tdd.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
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
                LocalDate.now().plusDays(7));
        when(loanService.loanBook("u-1", "b-1", loan.getReturnDate())).thenReturn(loan);

        ResponseEntity<LoanDTO> response = loanController
                .loanBook(new LoanDTO("b-1", "u-1", null, loan.getReturnDate(), null));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("b-1", response.getBody().getBookId());
        assertEquals("u-1", response.getBody().getUserId());
        assertEquals("ACTIVE", response.getBody().getStatus());
        verify(loanService).loanBook("u-1", "b-1", loan.getReturnDate());
    }

    @Test
    void getLoansShouldReturnAllLoans() {
        Loan loan1 = new Loan(new Book("Book 1", "Author 1", "b-1"), new User("u-1", "Ana"),
                LocalDate.now().plusDays(2));
        Loan loan2 = new Loan(new Book("Book 2", "Author 2", "b-2"), new User("u-2", "Luis"),
                LocalDate.now().plusDays(4));
        when(loanService.getAllLoans()).thenReturn(List.of(loan1, loan2));

        ResponseEntity<List<LoanDTO>> response = loanController.getLoans();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("b-1", response.getBody().get(0).getBookId());
        assertEquals("u-2", response.getBody().get(1).getUserId());
        verify(loanService).getAllLoans();
    }

    @Test
    void loanBookShouldPropagateLoanLimitExceededException() {
        LocalDate returnDate = LocalDate.now().plusDays(5);
        when(loanService.loanBook("u-1", "b-1", returnDate)).thenThrow(new LoanLimitExceededException());

        LoanLimitExceededException ex = assertThrows(LoanLimitExceededException.class,
                () -> loanController.loanBook(new LoanDTO("b-1", "u-1", null, returnDate, null)));

        assertEquals("Loan limit exceeded", ex.getMessage());
        verify(loanService).loanBook("u-1", "b-1", returnDate);
    }
}
