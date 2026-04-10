package edu.eci.dosw.tdd.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.UserDocumentMapper;
import edu.eci.dosw.tdd.persistence.nonrelational.repository.MongoUserRepository;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoUserRepository repository;

    public UserRepositoryMongoImpl(MongoUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return UserDocumentMapper.toModel(repository.save(UserDocumentMapper.toDocument(user)));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserDocumentMapper::toModel).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id).map(UserDocumentMapper::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(UserDocumentMapper::toModel);
    }
}
