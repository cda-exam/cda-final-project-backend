package fr.cda.cdafinalprojectbackend.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String description;
    private String profilePicture;
}
