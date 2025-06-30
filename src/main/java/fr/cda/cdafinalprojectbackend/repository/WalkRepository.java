package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long> {
}
