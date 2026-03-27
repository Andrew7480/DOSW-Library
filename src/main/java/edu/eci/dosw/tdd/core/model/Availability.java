package edu.eci.dosw.tdd.core.model;

import lombok.Data;

@Data
public class Availability {
    private String status;
    private int totalStock;
    private int availableStock;
    private int loanedStock;
}