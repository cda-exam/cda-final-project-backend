package fr.cda.cdafinalprojectbackend.entity;

import fr.cda.cdafinalprojectbackend.configuration.security.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            unique = true,
            nullable = false,
            name = "id"
    )
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(
            unique = true,
            nullable = false,
            name = "type"
    )
    private RoleEnum type;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<DBUser> users = new HashSet<>();
}
