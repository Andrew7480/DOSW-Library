package edu.eci.dosw.tdd.persistence.mapper;

import java.util.Locale;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.RoleEntity;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;

public class UserEntityMapper {

    private UserEntityMapper() {
    }

    public static User toModel(UserEntity entity) {
        return new User(entity.getId(), entity.getName());
    }

    public static UserEntity toEntity(User user, RoleEntity role, String username, String passwordHash) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setRole(role);
        entity.setUsername(resolveUsername(user.getName(), username));
        entity.setPasswordHash(passwordHash);
        return entity;
    }

    private static String resolveUsername(String name, String username) {
        if (username != null && !username.isBlank()) {
            return username;
        }
        return name.trim().toLowerCase(Locale.ROOT).replace(" ", ".");
    }
}
