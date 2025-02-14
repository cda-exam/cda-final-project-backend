package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.User;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getUsers();
    UserDTO getUserById(UUID id) throws UserNotFoundException;
    User createUser(UserCreateDTO userCreateDTO);
    User updateUser(UUID id, UserUpdateDTO userUpdateDTO) throws UserNotFoundException;
    void deleteUser(UUID id) throws UserNotFoundException;
}