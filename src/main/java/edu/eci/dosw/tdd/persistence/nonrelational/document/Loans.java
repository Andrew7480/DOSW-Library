package edu.eci.dosw.tdd.persistence.nonrelational.document;

import java.util.List;

import lombok.Data;

@Data
public class Loans {
    private String id;
        private String bookId;
        private String loanDate;
        private String returnDate;
        private String returnedAt;
        private List<StatusHistory> statusHistory;
}
