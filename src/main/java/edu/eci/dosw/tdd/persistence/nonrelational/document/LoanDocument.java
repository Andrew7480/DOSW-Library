package edu.eci.dosw.tdd.persistence.nonrelational.document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import lombok.Data;

@Data
public class LoanDocument {
    private String id;
        private String bookId;
        private LocalDate loanDate;
        private LocalDate returnDate;
        private LocalDateTime returnedAt;
        private List<StatusEventDocument> statusHistory;
}
