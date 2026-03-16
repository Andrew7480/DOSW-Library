package edu.eci.dosw.tdd.core.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void constructorShouldSetMessageStatusAndTimestamp() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ErrorResponse errorResponse = new ErrorResponse("Validation failed", 400);

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertEquals("Validation failed", errorResponse.getMessage());
        assertEquals(400, errorResponse.getStatus());
        assertNotNull(errorResponse.getTimestamp());
        assertTrue(!errorResponse.getTimestamp().isBefore(before));
        assertTrue(!errorResponse.getTimestamp().isAfter(after));
    }
}
