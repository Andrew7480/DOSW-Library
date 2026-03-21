package edu.eci.dosw.tdd.core.validator;

import org.springframework.stereotype.Component;

import edu.eci.dosw.tdd.core.exception.UserDeletionNotAllowedException;
import edu.eci.dosw.tdd.core.service.LoanService;

@Component
public class UserDeleteValidator {
    
    private final LoanService loanService;
    
    public UserDeleteValidator(LoanService loanService) {
        this.loanService = loanService;
    }
    
    public void validateUserCanBeDeleted(String userId) {
        if (loanService.userHasActiveLoan(userId)) {
            throw new UserDeletionNotAllowedException(userId);
        }
    }
}
