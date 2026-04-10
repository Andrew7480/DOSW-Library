package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepositoryMongoImpl;
import edu.eci.dosw.tdd.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryMongoImplTest {

    @Mock
    private MongoUserRepository userRepository;

    @Mock
    private MongoBookRepository bookRepository;

    private LoanRepositoryMongoImpl adapter;

    @BeforeEach
    void setUp() {
        adapter = new LoanRepositoryMongoImpl(userRepository, bookRepository);
    }

    @Test
    void saveShouldReplaceLoanInsideEmbeddedUserLoans() {
        UserDocument userDoc = new UserDocument();
        userDoc.setId("u-1");
        userDoc.setUsername("ana");

        LoanDocument existing = loanDocument("l-1", "b-1", LocalDate.now().plusDays(3));
        userDoc.setLoans(new ArrayList<>(List.of(existing)));

        when(userRepository.findByUsername("ana")).thenReturn(Optional.of(userDoc));
        when(userRepository.save(userDoc)).thenReturn(userDoc);

        Loan replacement = sampleLoan("l-1", "ana", "Clean Code", LocalDate.now().plusDays(10));
        Loan saved = adapter.save(replacement);

        assertEquals("l-1", saved.getId());
        verify(userRepository).save(argThat(u ->
                u.getLoans() != null
                        && u.getLoans().size() == 1
                        && LocalDate.now().plusDays(10).equals(u.getLoans().get(0).getReturnDate())));
    }

    @Test
    void findByIdShouldMapEmbeddedLoanUsingUserAndBookContext() {
        UserDocument userDoc = new UserDocument();
        userDoc.setId("u-1");
        userDoc.setName("Ana");
        userDoc.setUsername("ana");
        userDoc.setPasswordHash("hash");
        userDoc.setLoans(List.of(loanDocument("l-2", "b-2", LocalDate.now().plusDays(7))));

        BookDocument bookDoc = new BookDocument();
        bookDoc.setId("b-2");
        bookDoc.setTitle("Refactoring");

        when(userRepository.findByLoanId("l-2")).thenReturn(Optional.of(userDoc));
        when(bookRepository.findById("b-2")).thenReturn(Optional.of(bookDoc));

        Optional<Loan> result = adapter.findById("l-2");

        assertTrue(result.isPresent());
        assertEquals("ana", result.get().getUser().getUsername());
        assertEquals("Refactoring", result.get().getBook().getTitle());
        assertEquals(StatusLoanEnum.ACTIVE, result.get().getStatus());
    }

    @Test
    void findByUsernameAndBookTitleShouldFilterEmbeddedLoansByResolvedTitle() {
        UserDocument userDoc = new UserDocument();
        userDoc.setId("u-1");
        userDoc.setName("Ana");
        userDoc.setUsername("ana");
        userDoc.setPasswordHash("hash");
        userDoc.setLoans(List.of(loanDocument("l-3", "b-3", LocalDate.now().plusDays(5))));

        BookDocument bookDoc = new BookDocument();
        bookDoc.setId("b-3");
        bookDoc.setTitle("Clean Architecture");

        when(userRepository.findByUsername("ana")).thenReturn(Optional.of(userDoc));
        when(bookRepository.findById("b-3")).thenReturn(Optional.of(bookDoc));

        Optional<Loan> result = adapter.findByUsernameAndBookTitle("ana", "Clean Architecture");

        assertTrue(result.isPresent());
        assertEquals("l-3", result.get().getId());
        assertEquals("Clean Architecture", result.get().getBook().getTitle());
    }

    private Loan sampleLoan(String loanId, String username, String title, LocalDate returnDate) {
        User user = new User("u-1", username, username, "hash");
        Book book = new Book("b-1", title, "author", 5, 5);
        return new Loan(loanId, user, book, returnDate);
    }

    private LoanDocument loanDocument(String loanId, String bookId, LocalDate returnDate) {
        LoanDocument document = new LoanDocument();
        document.setId(loanId);
        document.setBookId(bookId);
        document.setLoanDate(LocalDate.now());
        document.setReturnDate(returnDate);
        return document;
    }
}
