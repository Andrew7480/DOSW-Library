package edu.eci.dosw.tdd.persistence.nonrelational.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.dosw.tdd.core.model.Role;
import lombok.Data;

import java.util.List;

@Document(collection = "users")
@Data
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;
    private Membership membership;
    private String createdAt;
    private List<Loans> loans;
    

}
