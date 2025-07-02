package fr.cda.cdafinalprojectbackend.dto.walk;

import fr.cda.cdafinalprojectbackend.dto.dog.DogDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class WalkDetailsDTO {
    private Long id;
    private LocalDateTime date;
    private Integer duration;
    private Integer participantsMax;
    private String description;
    private String location;
    private String startLatitude;
    private String startLongitude;
    private CreatedUserDTO createdBy;
    private Set<UserDTO> participantsUser = new HashSet<>();
    private Set<DogDTO> participantsDog = new HashSet<>();
}
