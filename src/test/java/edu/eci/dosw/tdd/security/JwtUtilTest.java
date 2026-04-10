package edu.eci.dosw.tdd.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.ExpiredJwtException;

class JwtUtilTest {

    private static final String SECRET = "MySuperSecretKeyForJWT1234567890";

    @Test
    void generateAndExtractClaimsShouldWork() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3600000);

        String token = jwtUtil.generateToken("u-1", "ana", "USER");

        assertNotNull(token);
        assertEquals("ana", jwtUtil.extractUsername(token));
        assertEquals("u-1", jwtUtil.extractId(token));
        assertEquals("USER", jwtUtil.extractRole(token));
        assertNotNull(jwtUtil.extractExpiration(token));
    }

    @Test
    void tokenShouldBeValidForMatchingUsername() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, 3600000);

        String token = jwtUtil.generateToken("u-2", "luis", "LIBRARIAN");

        assertTrue(jwtUtil.isTokenValid(token, "luis"));
        assertFalse(jwtUtil.isTokenValid(token, "other-user"));
    }

    @Test
    void tokenShouldBeInvalidWhenExpired() {
        JwtUtil jwtUtil = new JwtUtil(SECRET, -1);

        String token = jwtUtil.generateToken("u-3", "sofia", "USER");

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.isTokenValid(token, "sofia"));
    }
}
