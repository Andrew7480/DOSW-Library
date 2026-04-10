package edu.eci.dosw.tdd.persistence.nonrelational.document;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StatusEventDocument {
    private String status;
    private LocalDate date;
}
