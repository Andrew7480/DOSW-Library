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
    private int copies;

    public BookDTO() {
    }

    public BookDTO(String title, String author, int copies) {
        this.title = title;
        this.author = author;
        this.copies = copies;
    }

    public BookDTO(String id, String title, String author, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copies = copies;
    }

}
