package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getUsers();
    UserDTO getUserById(UUID id) throws UserNotFoundException;
    DBUser createUser(UserCreateDTO userCreateDTO);
    DBUser updateUser(UUID id, UserUpdateDTO userUpdateDTO) throws UserNotFoundException;
    void deleteUser(UUID id) throws UserNotFoundException;
    void activateUser(Map<String, String> activation, String email);
}