package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

public class LoanDocumentMapper {

    private LoanDocumentMapper() {
    }

    public static LoanDocument toDocument(Loan loan) {
        LoanDocument document = new LoanDocument();
        document.setId(loan.getId());
        document.setBookId(loan.getBook().getId());
        document.setLoanDate(loan.getLoanDate());
        document.setReturnDate(loan.getReturnDate());
        document.setReturnedAt(loan.getReturnedAt());
        return document;
    }

        public static Loan toDomain(LoanDocument document, UserDocument userDocument, String bookTitle) {
        User user = new User(
            userDocument.getId(),
            userDocument.getName() != null ? userDocument.getName() : userDocument.getUsername(),
            userDocument.getUsername(),
            userDocument.getPasswordHash() != null ? userDocument.getPasswordHash() : ""
        );

        Book book = new Book(
                document.getBookId(),
            bookTitle != null ? bookTitle : document.getBookId(),
                "",
                0,
                0
        );

        StatusLoanEnum status = document.getReturnedAt() == null ? StatusLoanEnum.ACTIVE : StatusLoanEnum.RETURNED;

        return new Loan(
                document.getId(),
                user,
                book,
                document.getLoanDate(),
                document.getReturnDate(),
                status,
            document.getReturnedAt()
        );
    }
}
