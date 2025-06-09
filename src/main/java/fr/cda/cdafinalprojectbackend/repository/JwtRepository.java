package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.entity.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValue(String value);
}
