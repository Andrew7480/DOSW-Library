package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;

public class LoanMapper {

	public static LoanDTO toDTO(Loan loan) {
		return new LoanDTO(
				loan.getBook().getId(),
				loan.getUser().getId(),
				loan.getLoanDate(),
				loan.getReturnDate(),
				loan.getStatus().name());
	}

	public static Loan toModel(LoanDTO dto, Book book, User user) {
		Loan loan = new Loan(book, user, dto.getReturnDate());
		if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
			loan.setStatus(StatusLoanEnum.valueOf(dto.getStatus().toUpperCase()));
		}
		return loan;
	}
}
