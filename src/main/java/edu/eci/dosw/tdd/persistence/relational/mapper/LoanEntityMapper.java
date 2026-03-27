package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;

public class LoanEntityMapper {

	private LoanEntityMapper() {
	}

	// Loan -> LoanEntity
	public static LoanEntity toEntity(Loan loan) {
		return new LoanEntity(
			loan.getId(),
			UserEntityMapper.toEntity(loan.getUser()),
			BookEntityMapper.toEntity(loan.getBook()),
			loan.getLoanDate(),
			loan.getReturnDate(),
			loan.getStatus(),
			loan.getReturnedAt()
		);
	}

	// LoanEntity -> Loan
	public static Loan toModel(LoanEntity entity) {
		return new Loan(
			entity.getId(),
			UserEntityMapper.toModel(entity.getUser()),
			BookEntityMapper.toModel(entity.getBook()),
			entity.getLoanDate(),
			entity.getReturnDate(),
			entity.getStatus(),
			entity.getReturnedAt()
		);
	}

}
