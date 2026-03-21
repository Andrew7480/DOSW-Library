package edu.eci.dosw.tdd.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public MessageResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
