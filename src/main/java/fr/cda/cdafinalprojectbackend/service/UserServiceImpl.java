package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Role;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.UserMapper;
import fr.cda.cdafinalprojectbackend.repository.RoleRepository;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import fr.cda.cdafinalprojectbackend.security.RoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserDTO> getUsers() {
        return userMapper.toDTOList(this.userRepository.findAll());
    }

    public UserDTO getUserById(UUID id) throws UserNotFoundException {
        DBUser dbUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDTO(dbUser);
    }

    @Transactional
    public DBUser createUser(UserCreateDTO userCreateDTO) throws UserAlreadyExistsException {
        boolean isUserAlreadyExists = userRepository.existsByEmail(userCreateDTO.getEmail());

        String passwordEncrypted = this.bCryptPasswordEncoder.encode(userCreateDTO.getPassword());

        if (isUserAlreadyExists) {
            throw new UserAlreadyExistsException();
        }

        Role role;
        Optional<Role> optionalRole = roleRepository.findRoleByType(RoleEnum.USER);

        if (optionalRole.isPresent()) {
            role = optionalRole.get();
        } else {
            role = new Role();
            role.setType(RoleEnum.USER);
        }

        DBUser dbUser = this.userMapper.toEntity(userCreateDTO);
        dbUser.setRole(role);
        dbUser.setIsActive(true);
        dbUser.setPassword(passwordEncrypted);

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public DBUser updateUser(UUID id, UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
        DBUser dbUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        dbUser.setNickname(userUpdateDTO.getNickname());
        dbUser.setDescription(userUpdateDTO.getDescription());
        dbUser.setProfilePicture(userUpdateDTO.getProfilePicture());

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public void deleteUser(UUID id) throws UserNotFoundException {
        DBUser dbUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        this.userRepository.delete(dbUser);
    }
}
