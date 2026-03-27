package edu.eci.dosw.tdd.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedResponseDTO {
    private String id;
    private String name;
    private String username;
    private String role;
}
