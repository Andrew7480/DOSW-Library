package edu.eci.dosw.tdd.persistence.nonrelational.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String username;
    private String email;
    private String passwordHash;
    private String role;
    private String membership;
    private String createdAt;
    private List<LoanInfo> loans;

    public static class LoanInfo {
        private String id;
        private String bookId;
        private String loanDate;
        private String returnDate;
        private String returnedAt;
        private List<StatusHistory> statusHistory;
    }
    public static class StatusHistory {
        private String status;
        private String date;
    }
    // getters y setters
}
