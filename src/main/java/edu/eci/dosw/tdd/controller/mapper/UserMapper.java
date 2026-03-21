package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;

public class UserMapper {

	private UserMapper() {
	}

	public static UserDTO toDTO(User user) {
		return new UserDTO(user.getId(), user.getName());
	}

	public static User toModel(UserDTO dto) {
		return new User(dto.getId(), dto.getName());
	}
}
