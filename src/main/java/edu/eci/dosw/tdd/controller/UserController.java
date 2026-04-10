package edu.eci.dosw.tdd.controller;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
import edu.eci.dosw.tdd.controller.dto.UserCreatedResponseDTO;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/users")
@Data
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserCreatedResponseDTO> registerUser(@Valid @RequestBody UserDTO request) {
		Role role = resolveRole(request.getRole());
		var user = userService.registerUser(request.getName(), request.getUsername(), request.getPassword(), role);
		var response = new UserCreatedResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getRole().name());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	private Role resolveRole(String role) {
		if (role == null || role.isBlank()) {
			return Role.USER;
		}
		try {
			return Role.valueOf(role.trim().toUpperCase());
		} catch (IllegalArgumentException exception) {
			return Role.USER;
		}
	}

	@GetMapping("/all")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<List<UserDTO>> getUsers() {
		List<UserDTO> response = userService.getUsers().stream().map(UserMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('LIBRARIAN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
		return ResponseEntity.ok(UserMapper.toDTO(userService.getUserById(id)));
	}

	@GetMapping("/{username}")
	@PreAuthorize("hasRole('LIBRARIAN') or #username == authentication.name")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
		return ResponseEntity.ok(UserMapper.toDTO(userService.getUserByUsername(username)));
	}

}
