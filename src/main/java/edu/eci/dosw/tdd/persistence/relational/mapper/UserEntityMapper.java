package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;

public class UserEntityMapper {

    private UserEntityMapper() {
    }

	// User -> UserEntity
	public static UserEntity toEntity(User user) {
		return new UserEntity(user.getId(), user.getName(), user.getUsername(), user.getPasswordHash(), user.getRole());
	}

	// UserEntity -> User
	public static User toModel(UserEntity entity) {
		return new User(entity.getId(), entity.getName(), entity.getUsername(), entity.getPasswordHash(), entity.getRole());
	}

}
