package fr.cda.cdafinalprojectbackend.mapper;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilPicture(user.getProfilePicture());
        userDTO.setDescription(user.getDescription());
        userDTO.setRole(user.getRole());
        userDTO.setIsActive(user.getIsActive());

        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setProfilePicture(userDTO.getProfilPicture());
        user.setDescription(userDTO.getDescription());
        user.setRole(userDTO.getRole());
        user.setIsActive(userDTO.getIsActive());
        return user;
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<User> toEntityList(List<UserDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    public User toEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setDescription(userCreateDTO.getDescription());
        user.setProfilePicture(userCreateDTO.getProfilPicture());
        user.setPassword(userCreateDTO.getPassword());

        return user;
    }

    public User toEntity(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null) {
            return null;
        }

        User user = new User();

        user.setUsername(userUpdateDTO.getUsername());
        user.setProfilePicture(userUpdateDTO.getProfilPicture());
        user.setDescription(userUpdateDTO.getDescription());
        return user;
    }
}
