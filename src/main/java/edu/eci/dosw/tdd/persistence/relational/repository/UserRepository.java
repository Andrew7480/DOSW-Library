package edu.eci.dosw.tdd.persistence.relational.repository;


import java.util.List;
import java.util.Optional;
import edu.eci.dosw.tdd.core.model.Role;
import edu.eci.dosw.tdd.persistence.relational.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	// Buscar usuario por username (login)
	Optional<UserEntity> findByUsername(String username);

	// Buscar usuarios por rol
	List<UserEntity> findByRole(Role role);

}
