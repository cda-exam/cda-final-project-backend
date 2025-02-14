package fr.cda.cdafinalprojectbackend.dto.user;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String username;
    private String password;
    private String email;
    private String description;
    private String profilePicture;
}
