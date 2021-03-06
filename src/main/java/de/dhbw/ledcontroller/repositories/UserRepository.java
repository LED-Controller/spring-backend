package de.dhbw.ledcontroller.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dhbw.ledcontroller.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByName(String name);

	Boolean existsByName(String name);
}