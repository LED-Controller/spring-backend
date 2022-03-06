package de.dhbw.ledcontroller.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.dhbw.ledcontroller.models.Lamp;

@Repository
public interface LampRepository extends JpaRepository<Lamp, String> {
    public List<Lamp> findAllByOrderByNameAsc();

    public Optional<Lamp> findByMac(String mac);
}