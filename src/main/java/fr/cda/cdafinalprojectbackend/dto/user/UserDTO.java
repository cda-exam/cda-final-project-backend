package fr.cda.cdafinalprojectbackend.dto.user;

import fr.cda.cdafinalprojectbackend.configuration.security.RoleEnum;
import fr.cda.cdafinalprojectbackend.dto.role.RoleDTO;
import fr.cda.cdafinalprojectbackend.entity.Role;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String nickname;
    private String email;
    private String description;
    private String profilePicture;
    private String city;
    private RoleEnum role;
    private Boolean isActive;
}
