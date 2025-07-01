package fr.cda.cdafinalprojectbackend.dto.walk;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class WalkLocationDTO {
    private Long id;
    private String latitude;
    private String longitude;
    private UUID createdBy;
}
