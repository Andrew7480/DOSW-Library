package edu.eci.dosw.tdd.persistence.relational.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.LoanRepositoryJpaImpl;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanEntityMapper;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryJpaImplTest {

    @Mock
    private LoanRepository jpaRepository;

    private LoanRepositoryJpaImpl adapter;

    @BeforeEach
    void setUp() {
        adapter = new LoanRepositoryJpaImpl(jpaRepository);
    }

    @Test
    void saveShouldMapDomainToEntityAndBack() {
        Loan loan = sampleLoan("l-1", "ana", "Clean Code");

        when(jpaRepository.save(any(LoanEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan saved = adapter.save(loan);

        assertEquals("l-1", saved.getId());
        assertEquals("ana", saved.getUser().getUsername());
        assertEquals("Clean Code", saved.getBook().getTitle());
        verify(jpaRepository).save(any(LoanEntity.class));
    }

    @Test
    void findByUsernameShouldReturnMappedLoans() {
        Loan loan = sampleLoan("l-2", "luis", "DDD");
        when(jpaRepository.findByUser_Username("luis"))
                .thenReturn(List.of(LoanEntityMapper.toEntity(loan)));

        List<Loan> result = adapter.findByUsername("luis");

        assertEquals(1, result.size());
        assertEquals("luis", result.get(0).getUser().getUsername());
        assertEquals("DDD", result.get(0).getBook().getTitle());
    }

    @Test
    void findByUsernameAndBookTitleShouldDelegateToJpaRepository() {
        Loan loan = sampleLoan("l-3", "ana", "Refactoring");
        when(jpaRepository.findByUser_UsernameAndBook_Title("ana", "Refactoring"))
                .thenReturn(Optional.of(LoanEntityMapper.toEntity(loan)));

        Optional<Loan> result = adapter.findByUsernameAndBookTitle("ana", "Refactoring");

        assertTrue(result.isPresent());
        assertEquals("l-3", result.get().getId());
    }

    private Loan sampleLoan(String loanId, String username, String title) {
        User user = new User("u-1", username, username, "hash");
        Book book = new Book("b-1", title, "author", 5, 5);
        return new Loan(loanId, user, book, LocalDate.now().plusDays(7));
    }
}
