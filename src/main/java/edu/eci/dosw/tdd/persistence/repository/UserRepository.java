package edu.eci.dosw.tdd.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.dosw.tdd.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByNameIgnoreCase(String name);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    boolean existsByNameIgnoreCase(String name);
}
