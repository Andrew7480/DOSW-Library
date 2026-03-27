package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookDTO {

	private String id;
    @NotBlank
	private String title;
    @NotBlank
	private String author;
    @Positive
    private int stock;
    @Positive
    private int availableCopies;

    public BookDTO() {
    }

    public BookDTO(String id, String title, String author, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.availableCopies = stock;
    }

    public BookDTO(String id, String title, String author, int stock, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.stock = stock;
        this.availableCopies = availableCopies;
    }

}
