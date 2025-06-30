package fr.cda.cdafinalprojectbackend.dto.walk;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class WalkDTO {
    private Long id;
    private LocalDateTime date;
    private Integer duration;
    private Integer participantsMax;
    private String description;
    private String location;
    private Long startLatitude;
    private Long startLongitude;
    private UUID createdBy;
    private Set<UUID> participants = new HashSet<>();
}
