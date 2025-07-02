package fr.cda.cdafinalprojectbackend.dto.walk;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatedUserDTO {
    private UUID id;
    private String username;
}