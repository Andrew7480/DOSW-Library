package edu.eci.dosw.tdd.controller;

import java.util.List;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.controller.mapper.UserMapper;
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

@RestController
@RequestMapping("/users")
@Data
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO request) {
		return new ResponseEntity<>(UserMapper.toDTO(userService.registerUser(request.getName(), request.getUsername(), request.getPassword(), Role.valueOf(request.getRole()))), HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<UserDTO>> getUsers() {
		List<UserDTO> response = userService.getUsers().stream().map(UserMapper::toDTO).toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
		return ResponseEntity.ok(UserMapper.toDTO(userService.getUserById(id)));
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
		return ResponseEntity.ok(UserMapper.toDTO(userService.getUserByUsername(username)));
	}

}
