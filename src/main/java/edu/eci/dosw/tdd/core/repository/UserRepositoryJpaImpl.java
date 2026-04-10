package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.relational.mapper.UserEntityMapper;

@Repository
@Profile("relational")
public class UserRepositoryJpaImpl implements UserRepository {

    private final edu.eci.dosw.tdd.persistence.relational.repository.UserRepository repository;

    public UserRepositoryJpaImpl(edu.eci.dosw.tdd.persistence.relational.repository.UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserEntityMapper.toModel(repository.save(UserEntityMapper.toEntity(user)));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserEntityMapper::toModel).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(UserEntityMapper::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserEntityMapper::toModel);
    }
}
