package edu.eci.dosw.tdd.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

	private String id;
    @NotBlank
	private String name;

    public UserDTO() {
    }

    public UserDTO( String name) {
        this.name = name;
    }

    public UserDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
