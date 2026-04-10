package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import edu.eci.dosw.tdd.core.model.Loan;

public interface LoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    List<Loan> findAll();
    void delete(String id);

    List<Loan> findByUsername(String username);

    Optional<Loan> findByUsernameAndBookTitle(String username, String bookTitle);
}
