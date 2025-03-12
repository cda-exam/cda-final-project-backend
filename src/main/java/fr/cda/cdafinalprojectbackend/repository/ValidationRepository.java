package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Optional<Validation> findByCode(String code);
}
