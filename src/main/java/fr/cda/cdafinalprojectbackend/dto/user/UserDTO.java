package fr.cda.cdafinalprojectbackend.dto.user;

import fr.cda.cdafinalprojectbackend.security.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String description;
    private String profilPicture;
    private Role role;
    private Boolean isActive;
}
