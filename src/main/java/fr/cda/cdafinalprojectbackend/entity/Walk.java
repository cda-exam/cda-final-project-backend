package fr.cda.cdafinalprojectbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime date;
    private Integer duration;
    private Integer participantsMax;
    private String description;
    private String location;
    private Long startLatitude;
    private Long startLongitude;
    
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private DBUser createdBy;
    
    @ManyToMany
    @JoinTable(
        name = "user_walks",
        joinColumns = @JoinColumn(name = "walk_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<DBUser> participants = new HashSet<>();
}
