package edu.eci.dosw.tdd.persistence.nonrelational.document;

import lombok.Data;

@Data
public class AvailabilityDocument {
    private String status;
    private int totalStock;
    private int availableStock;
    private int loanedStock;
}