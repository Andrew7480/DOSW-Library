package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import edu.eci.dosw.tdd.core.model.User;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);
}
