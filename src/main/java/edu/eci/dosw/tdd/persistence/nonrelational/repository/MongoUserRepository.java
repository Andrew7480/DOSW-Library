package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;


public interface MongoUserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String username);

    Optional<UserDocument> findByEmail(String email);

    List<UserDocument> findByMembership(String membership);

    @Query("{ 'loans.id': ?0 }")
    Optional<UserDocument> findByLoanId(String loanId);

}
