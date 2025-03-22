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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final String NICKNAME_TEST = "username";
    public static final String PASSWORD_TEST = "password";
    public static final String EMAIL_TEST = "email@test.fr";
    public static final String PROFILE_PICTURE_TEST = "profilePicture.jpg";
    public static final String DESCRIPTION_TEST = "description";
    public static final String UPDATED_NICKNAME = "updatedUsername";
    public static final String UPDATED_DESCRIPTION = "updatedDescription";
    public static final String UPDATED_PROFILE_PICTURE = "updatedProfilePicture.jpg";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ValidationServiceImpl validationService;

    @Mock
    private ValidationRepository validationRepository;

    private DBUser user;
    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;
    private UUID uuid;
    private UserUpdateDTO userUpdateDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new DBUser();
        userDTO = new UserDTO();
        userCreateDTO = new UserCreateDTO();
        userUpdateDTO = new UserUpdateDTO();
        role = new Role();
        role.setType(RoleEnum.USER);

        user.setId(uuid);
        user.setNickname(NICKNAME_TEST);
        user.setPassword(PASSWORD_TEST);
        user.setEmail(EMAIL_TEST);
        user.setProfilePicture(PROFILE_PICTURE_TEST);
        user.setDescription(DESCRIPTION_TEST);
        user.setRole(role);
        user.setIsActive(true);

        userDTO.setNickname(NICKNAME_TEST);
        userDTO.setEmail(EMAIL_TEST);
        userDTO.setProfilePicture(PROFILE_PICTURE_TEST);
        userDTO.setDescription(DESCRIPTION_TEST);
        userDTO.setRole(role);
        userDTO.setIsActive(true);

        userCreateDTO.setNickname(NICKNAME_TEST);
        userCreateDTO.setEmail(EMAIL_TEST);
        userCreateDTO.setPassword(PASSWORD_TEST);
        userCreateDTO.setDescription(DESCRIPTION_TEST);
        userCreateDTO.setProfilePicture(PROFILE_PICTURE_TEST);

        userUpdateDTO.setNickname(UPDATED_NICKNAME);
        userUpdateDTO.setDescription(UPDATED_DESCRIPTION);
        userUpdateDTO.setProfilePicture(UPDATED_PROFILE_PICTURE);
    }

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<DBUser> userList = Arrays.asList(user);
        List<UserDTO> userDTOList = Arrays.asList(userDTO);
        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDTOList(userList)).thenReturn(userDTOList);

        // Act
        List<UserDTO> result = userService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(NICKNAME_TEST, result.get(0).getNickname());
        verify(userRepository).findAll();
        verify(userMapper).toDTOList(userList);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(uuid);

        assertNotNull(result);
        assertEquals(NICKNAME_TEST, result.getNickname());
        assertEquals(EMAIL_TEST, result.getEmail());
        assertEquals(DESCRIPTION_TEST, result.getDescription());
        assertEquals(PROFILE_PICTURE_TEST, result.getProfilePicture());
        assertEquals(RoleEnum.USER, result.getRole().getType());
        assertTrue(result.getIsActive());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(uuid));

        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void createUser_ShouldCreateUser_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsByEmail(EMAIL_TEST)).thenReturn(false);
        when(bCryptPasswordEncoder.encode(PASSWORD_TEST)).thenReturn("encoded_password");
        when(roleRepository.findRoleByType(RoleEnum.USER)).thenReturn(Optional.of(role));
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);
        when(userRepository.save(any(DBUser.class))).thenReturn(user);
        doNothing().when(validationService).saveValidation(any(DBUser.class));

        // Act
        DBUser result = userService.createUser(userCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(NICKNAME_TEST, result.getNickname());
        assertEquals(EMAIL_TEST, result.getEmail());
        verify(userRepository).save(any(DBUser.class));
        verify(validationService).saveValidation(any(DBUser.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByEmail(EMAIL_TEST)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));

        verify(userRepository).existsByEmail(EMAIL_TEST);
        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(DBUser.class))).thenReturn(user);

        DBUser updatedUser = userService.updateUser(uuid, userUpdateDTO);

        assertNotNull(updatedUser);
        assertEquals(UPDATED_NICKNAME, updatedUser.getNickname());
        assertEquals(UPDATED_DESCRIPTION, updatedUser.getDescription());
        assertEquals(UPDATED_PROFILE_PICTURE, updatedUser.getProfilePicture());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(uuid, userUpdateDTO));

        verify(userRepository).findById(uuid);
        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        userService.deleteUser(uuid);

        verify(userRepository).findById(uuid);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));

        verify(userRepository).findById(uuid);
        verify(userRepository, never()).delete(any(DBUser.class));
    }

    @Test
    void loadUserByUsername_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail(EMAIL_TEST)).thenReturn(Optional.of(user));

        DBUser result = userService.loadUserByUsername(EMAIL_TEST);

        assertNotNull(result);
        assertEquals(EMAIL_TEST, result.getEmail());
        verify(userRepository).findByEmail(EMAIL_TEST);
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(EMAIL_TEST)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL_TEST));
        verify(userRepository).findByEmail(EMAIL_TEST);
    }

    @Test
    void activateUser_ShouldActivateUser_WhenValidCodeProvided() {
        // Arrange
        Map<String, String> activation = Map.of("code", "valid_code");
        Validation validation = new Validation();
        validation.setUser(user);
        validation.setExpiration(Instant.now().plusSeconds(3600)); // 1 hour in future

        when(validationService.getValidationByCode("valid_code")).thenReturn(validation);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(DBUser.class))).thenReturn(user);
        when(validationRepository.save(any(Validation.class))).thenReturn(validation);

        // Act
        userService.activateUser(activation);

        // Assert
        assertTrue(user.getIsActive());
        verify(userRepository).save(user);
        verify(validationRepository).save(validation);
    }

    @Test
    void activateUser_ShouldThrowException_WhenCodeExpired() {
        // Arrange
        Map<String, String> activation = Map.of("code", "expired_code");
        Validation validation = new Validation();
        validation.setUser(user);
        validation.setExpiration(Instant.now().minusSeconds(3600)); // 1 hour in past

        when(validationService.getValidationByCode("expired_code")).thenReturn(validation);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.activateUser(activation));
    }

    @Test
    void activateUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Map<String, String> activation = Map.of("code", "valid_code");
        Validation validation = new Validation();
        validation.setUser(user);
        validation.setExpiration(Instant.now().plusSeconds(3600));

        when(validationService.getValidationByCode("valid_code")).thenReturn(validation);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.activateUser(activation));
    }
}
