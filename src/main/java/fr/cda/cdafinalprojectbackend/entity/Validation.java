package fr.cda.cdafinalprojectbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant creation;

    private Instant expiration;

    private Instant activation;

    private String code;

    @OneToOne(cascade = CascadeType.ALL)
    private DBUser user;
}
