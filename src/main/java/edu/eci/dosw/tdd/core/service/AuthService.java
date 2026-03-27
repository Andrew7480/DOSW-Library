package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.AuthenticationException;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.util.PasswordHashUtil;
import edu.eci.dosw.tdd.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public String authenticateAndGenerateToken(String username, String password) {
        User user = userService.getUserByUsername(username);
        if (PasswordHashUtil.verifyPassword(password, user.getPasswordHash())) {
            return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        } else {
            throw new AuthenticationException(username);
        }
    }
}
