package fr.cda.cdafinalprojectbackend.entity;

import fr.cda.cdafinalprojectbackend.security.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@DynamicUpdate // Permet d'update uniquement les champs modifiés
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            unique = true,
            nullable = false,
            name = "id"
    )
    private UUID id;

    @Column(
            unique = true,
            nullable = false,
            name = "username"
    )
    private String username;

    @Column(
            unique = true,
            nullable = false,
            name = "email"
    )
    private String email;

    @Column(
            unique = false,
            nullable = false,
            name = "password"
    )
    private String password;

    @Column(
            unique = false,
            nullable = true,
            name = "description"
    )
    private String description;

    @Column(
            unique = false,
            nullable = true,
            name = "profile_picture"
    )
    private String profilePicture;

    @Column(
            nullable = false,
            unique = false,
            name = "is_active"
    )
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            unique = false,
            name = "role"
    )
    private Role role;

    public void setPassword(String rawPassword) {
        this.password = new BCryptPasswordEncoder().encode(rawPassword);
    }
}
