package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.User;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.UserMapper;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import fr.cda.cdafinalprojectbackend.security.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getUsers() {
        return userMapper.toDTOList(this.userRepository.findAll());
    }

    public UserDTO getUserById(UUID id) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDTO(user);
    }

    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        boolean isUserAlreadyExists = userRepository.existsByEmail(userCreateDTO.getEmail());

        if (isUserAlreadyExists) {
            throw new UserAlreadyExistsException();
        }

        User user = this.userMapper.toEntity(userCreateDTO);
        user.setRole(Role.ROLE_USER);
        user.setIsActive(true);
        return this.userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        user.setUsername(userUpdateDTO.getUsername());
        user.setDescription(userUpdateDTO.getDescription());
        user.setProfilePicture(userUpdateDTO.getProfilePicture());

        return this.userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) throws UserNotFoundException {
        User user = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        this.userRepository.delete(user);
    }
}
