package edu.eci.dosw.tdd.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.entity.LoanEntity;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    List<LoanEntity> findByUser_NameIgnoreCase(String userName);

    List<LoanEntity> findByStatus_NameIgnoreCase(String statusName);

    List<LoanEntity> findByUser_NameIgnoreCaseAndStatus_NameIgnoreCase(String userName, String statusName);

    List<LoanEntity> findByReturnDateBeforeAndStatus_NameIgnoreCase(LocalDate date, String statusName);

    Optional<LoanEntity> findFirstByUser_NameIgnoreCaseAndBook_TitleIgnoreCaseAndStatus_NameIgnoreCase(
            String userName,
            String bookTitle,
            String statusName);

    long countByUser_IdAndStatus_NameIgnoreCase(String userId, String statusName);
}
