package edu.eci.dosw.tdd.controller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import edu.eci.dosw.tdd.controller.dto.BookDTO;
import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.Book;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.StatusLoanEnum;
import edu.eci.dosw.tdd.core.model.User;
import org.junit.jupiter.api.Test;

class MapperTest {

    @Test
    void bookMapperShouldMapModelToDtoAndBack() {
        Book book = new Book("Clean Code", "Robert C. Martin", "b-1");

        BookDTO dto = BookMapper.toDTO(book, 3);
        Book mappedBack = BookMapper.toModel(dto);

        assertEquals("b-1", dto.getId());
        assertEquals("Clean Code", dto.getTitle());
        assertEquals("Robert C. Martin", dto.getAuthor());
        assertEquals(3, dto.getCopies());

        assertEquals("b-1", mappedBack.getId());
        assertEquals("Clean Code", mappedBack.getTitle());
        assertEquals("Robert C. Martin", mappedBack.getAuthor());
    }

    @Test
    void userMapperShouldMapModelToDtoAndBack() {
        User user = new User("u-1", "Ana");

        UserDTO dto = UserMapper.toDTO(user);
        User mappedBack = UserMapper.toModel(dto);

        assertEquals("u-1", dto.getId());
        assertEquals("Ana", dto.getName());

        assertEquals("u-1", mappedBack.getId());
        assertEquals("Ana", mappedBack.getName());
    }

    @Test
    void loanMapperShouldMapModelToDtoAndBack() {
        Book book = new Book("DDD", "Eric Evans", "b-1");
        User user = new User("u-1", "Ana");
        Loan loan = new Loan(book, user, LocalDate.now().plusDays(7));
        loan.setStatus(StatusLoanEnum.RETURNED);

        LoanDTO dto = LoanMapper.toDTO(loan);
        Loan mappedBack = LoanMapper.toModel(dto, book, user);

        assertEquals("b-1", dto.getBookId());
        assertEquals("u-1", dto.getUserId());
        assertEquals(StatusLoanEnum.RETURNED.name(), dto.getStatus());
        assertEquals(loan.getReturnDate(), dto.getReturnDate());

        assertEquals("b-1", mappedBack.getBook().getId());
        assertEquals("u-1", mappedBack.getUser().getId());
        assertEquals(StatusLoanEnum.RETURNED, mappedBack.getStatus());
        assertEquals(dto.getReturnDate(), mappedBack.getReturnDate());
    }
}
