package edu.eci.dosw.tdd.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loan_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name; // ACTIVE, RETURNED
}
