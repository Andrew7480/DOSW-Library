package edu.eci.dosw.tdd.persistence.relational.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
	// Préstamos de un usuario segun status
	List<LoanEntity> findByUserIdAndStatus(String userId, StatusLoanEnum status);

	// Préstamos de un libro específico
	List<LoanEntity> findByBookId(String bookId);

	// Préstamos vencidos (fecha de retorno pasada y no devueltos)
	List<LoanEntity> findByReturnDateBeforeAndStatus(LocalDate date, StatusLoanEnum status);

	// Préstamos entre fechas
	List<LoanEntity> findByLoanDateBetween(LocalDate start, LocalDate end);



	List<LoanEntity> findByUser_Username(String username);

	Optional<LoanEntity> findByUser_UsernameAndBook_Title(String username, String title);

}
