package fr.cda.cdafinalprojectbackend.dto.user;

import fr.cda.cdafinalprojectbackend.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String nickname;
    private String email;
    private String description;
    private String profilePicture;
    private Role role;
    private Boolean isActive;
}
