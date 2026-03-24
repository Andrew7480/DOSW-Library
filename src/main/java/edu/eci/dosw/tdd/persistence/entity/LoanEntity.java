package edu.eci.dosw.tdd.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.eci.dosw.tdd.core.model.StatusLoanEnum;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;
    
    @Column(nullable = false)
    private LocalDate loanDate;
    
    @Column(nullable = false)
    private LocalDate returnDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLoanEnum status;
    
    @Column(nullable = true)
    private LocalDateTime returnedAt;
}
