package fr.cda.cdafinalprojectbackend.repository;

import fr.cda.cdafinalprojectbackend.entity.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValue(String value);

    @Query("FROM Jwt j WHERE j.user.email = :email AND j.isActive = :active AND j.expirate = :expirate")
    Optional<Jwt> findValidTokenByUser(String email, boolean active, boolean expirate);

    @Query("FROM Jwt j WHERE j.user.email = :email")
    Stream<Jwt> findTokenByUser(String email);

    void deleteAllByIsActiveAndExpirate(boolean isActive, boolean expirate);
}