package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanEntityMapper;
import edu.eci.dosw.tdd.persistence.relational.repository.LoanRepository;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements edu.eci.dosw.tdd.core.repository.LoanRepository {

    private final LoanRepository repository;

    public LoanRepositoryJpaImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return LoanEntityMapper.toModel(
                repository.save(LoanEntityMapper.toEntity(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id).map(LoanEntityMapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll().stream()
                .map(LoanEntityMapper::toModel)
                .toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<Loan> findByUsername(String username) {
        return repository.findByUser_Username(username).stream()
                .map(LoanEntityMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Loan> findByUsernameAndBookTitle(String username, String bookTitle) {
        return repository.findByUser_UsernameAndBook_Title(username, bookTitle)
                .map(LoanEntityMapper::toModel);
    }
}
