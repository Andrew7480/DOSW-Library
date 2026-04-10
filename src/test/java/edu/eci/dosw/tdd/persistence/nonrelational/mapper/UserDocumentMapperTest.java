package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

class UserDocumentMapperTest {

    @Test
    void toDocumentShouldMapAllMainFields() {
        User user = new User("u-1", "Ana", "ana", "hash", Role.LIBRARIAN);

        UserDocument document = UserDocumentMapper.toDocument(user);

        assertEquals("u-1", document.getId());
        assertEquals("Ana", document.getName());
        assertEquals("ana", document.getUsername());
        assertEquals("hash", document.getPasswordHash());
        assertEquals(Role.LIBRARIAN, document.getRole());
    }

    @Test
    void toModelShouldUseUserRoleAsDefaultWhenRoleIsNull() {
        UserDocument document = new UserDocument();
        document.setId("u-2");
        document.setName("Luis");
        document.setUsername("luis");
        document.setPasswordHash("hash");
        document.setRole(null);

        User model = UserDocumentMapper.toModel(document);

        assertEquals(Role.USER, model.getRole());
    }

    @Test
    void toModelShouldPreserveExistingRole() {
        UserDocument document = new UserDocument();
        document.setId("u-3");
        document.setName("Sofia");
        document.setUsername("sofia");
        document.setPasswordHash("hash");
        document.setRole(Role.LIBRARIAN);

        User model = UserDocumentMapper.toModel(document);

        assertEquals("u-3", model.getId());
        assertEquals("Sofia", model.getName());
        assertEquals("sofia", model.getUsername());
        assertEquals("hash", model.getPasswordHash());
        assertEquals(Role.LIBRARIAN, model.getRole());
    }
}
