package fr.cda.cdafinalprojectbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "dogs")
@Getter
@Setter
@DynamicUpdate
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthday;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false, length = 10)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private DBUser user;
}
