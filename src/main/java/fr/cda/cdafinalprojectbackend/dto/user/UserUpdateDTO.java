package fr.cda.cdafinalprojectbackend.dto.user;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nickname;
    private String description;
    private String profilePicture;
    private String city;
}
