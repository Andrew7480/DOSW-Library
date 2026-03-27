package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;

import java.util.List;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    @Query("{username: '?0'}")
    UserDocument findUserByUsername(String username);

}
