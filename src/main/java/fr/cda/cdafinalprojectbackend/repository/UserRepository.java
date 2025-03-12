package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<DBUser, UUID> {
    boolean existsByEmail(String email);
}