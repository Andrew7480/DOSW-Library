package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

public class UserDocumentMapper {

    private UserDocumentMapper() {
    }

    public static UserDocument toDocument(User user) {
        UserDocument document = new UserDocument();
        document.setId(user.getId());
        document.setName(user.getName());
        document.setUsername(user.getUsername());
        document.setPasswordHash(user.getPasswordHash());
        document.setRole(user.getRole());
        return document;
    }

    public static User toModel(UserDocument document) {
        Role role = document.getRole() == null ? Role.USER : document.getRole();
        return new User(
                document.getId(),
                document.getName(),
                document.getUsername(),
                document.getPasswordHash(),
                role
        );
    }
}
