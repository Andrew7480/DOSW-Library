package edu.eci.dosw.tdd.core.validator;

import java.time.LocalDate;

import edu.eci.dosw.tdd.core.util.DateUtil;
import edu.eci.dosw.tdd.core.util.ValidationUtil;

public class LoanValidator {

	private LoanValidator() {
	}

	public static void validateLoanRequest(String userId, String bookId, LocalDate returnDate) {
		ValidationUtil.requireNotBlank(userId, "userId");
		ValidationUtil.requireNotBlank(bookId, "bookId");
		ValidationUtil.requireNotNull(returnDate, "fecha de devolucion");
		ValidationUtil.requireTrue(DateUtil.isTodayOrFuture(returnDate),
				"la fecha de devolucion debe ser hoy o futura");
	}

	public static void validateLoanDates(LocalDate loanDate, LocalDate returnDate) {
		ValidationUtil.requireNotNull(loanDate, "fecha de prestamo");
		ValidationUtil.requireNotNull(returnDate, "fecha de devolucion");
		ValidationUtil.requireTrue(!returnDate.isBefore(loanDate),
				"la fecha de devolucion no puede ser anterior a la fecha de prestamo");
	}

}
