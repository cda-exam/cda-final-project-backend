package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDTO toDTO(DBUser dbUser) {
        if (dbUser == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setNickname(dbUser.getNickname());
        userDTO.setEmail(dbUser.getEmail());
        userDTO.setProfilePicture(dbUser.getProfilePicture());
        userDTO.setDescription(dbUser.getDescription());
        userDTO.setRole(dbUser.getRole());
        userDTO.setIsActive(dbUser.getIsActive());
        userDTO.setCity(dbUser.getCity());

        return userDTO;
    }

    public DBUser toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        DBUser dbUser = new DBUser();

        dbUser.setNickname(userDTO.getNickname());
        dbUser.setEmail(userDTO.getEmail());
        dbUser.setProfilePicture(userDTO.getProfilePicture());
        dbUser.setDescription(userDTO.getDescription());
        dbUser.setRole(userDTO.getRole());
        dbUser.setIsActive(userDTO.getIsActive());
        dbUser.setCity(userDTO.getCity());
        return dbUser;
    }

    public List<UserDTO> toDTOList(List<DBUser> dbUsers) {
        return dbUsers.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<DBUser> toEntityList(List<UserDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    public DBUser toEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }

        DBUser dbUser = new DBUser();

        dbUser.setNickname(userCreateDTO.getNickname());
        dbUser.setEmail(userCreateDTO.getEmail());
        dbUser.setDescription(userCreateDTO.getDescription());
        dbUser.setProfilePicture(userCreateDTO.getProfilePicture());
        dbUser.setPassword(userCreateDTO.getPassword());
        dbUser.setCity(userCreateDTO.getCity());

        return dbUser;
    }

    public DBUser toEntity(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null) {
            return null;
        }

        DBUser dbUser = new DBUser();

        dbUser.setNickname(userUpdateDTO.getNickname());
        dbUser.setProfilePicture(userUpdateDTO.getProfilePicture());
        dbUser.setDescription(userUpdateDTO.getDescription());
        dbUser.setCity(userUpdateDTO.getCity());
        return dbUser;
    }
}
