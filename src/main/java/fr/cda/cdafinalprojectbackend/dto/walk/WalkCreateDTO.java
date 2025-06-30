package fr.cda.cdafinalprojectbackend.dto.walk;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WalkCreateDTO {
    private LocalDateTime date;
    private Integer duration;
    private Integer participantsMax;
    private String description;
    private String location;
    private Long startLatitude;
    private Long startLongitude;
}