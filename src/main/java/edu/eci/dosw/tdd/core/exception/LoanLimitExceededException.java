package edu.eci.dosw.tdd.core.exception;

import org.springframework.http.HttpStatus;

public class LoanLimitExceededException extends LibraryException {

	public LoanLimitExceededException() {
        super("Loan limit exceeded", HttpStatus.CONFLICT);
    }

}
