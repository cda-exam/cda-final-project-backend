package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserUpdateDTO;
import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Role;
import fr.cda.cdafinalprojectbackend.entity.Validation;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.UserMapper;
import fr.cda.cdafinalprojectbackend.repository.RoleRepository;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import fr.cda.cdafinalprojectbackend.repository.ValidationRepository;
import fr.cda.cdafinalprojectbackend.configuration.security.RoleEnum;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ValidationService validationService;
    private ValidationRepository validationRepository;

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
        dbUser.setIsActive(false);
        dbUser.setPassword(passwordEncrypted);

        dbUser = this.userRepository.save(dbUser);
        this.validationService.saveValidation(dbUser);
        return dbUser;
    }

    @Transactional
    public DBUser updateUser(UUID id, UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
        DBUser dbUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        dbUser.setNickname(userUpdateDTO.getNickname());
        dbUser.setDescription(userUpdateDTO.getDescription());
        dbUser.setProfilePicture(userUpdateDTO.getProfilePicture());
        dbUser.setCity(userUpdateDTO.getCity());

        return this.userRepository.save(dbUser);
    }

    @Transactional
    public void deleteUser(UUID id) throws UserNotFoundException {
        DBUser dbUser = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        this.userRepository.delete(dbUser);
    }

    @Transactional
    public void activateUser(Map<String, String> activation, String email) {
        Validation validation = this.validationService.getValidationByCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Invalid code");
        }

        if (!validation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Invalid code");
        }

        DBUser dbUser = userRepository.findById(validation.getUser().getId()).orElseThrow(
                () -> new RuntimeException("Unknown user")
        );

        dbUser.setIsActive(true);
        this.userRepository.save(dbUser);
        validation.setActivation(Instant.now());
        validationRepository.save(validation);
    }

    @Override
    public DBUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByEmailAndIsActiveIsTrue(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not found")
                );
    }
}
