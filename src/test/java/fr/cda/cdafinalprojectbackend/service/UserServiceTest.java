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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String USERNAME_TEST = "username";
    public static final String PASSWORD_TEST = "password";
    public static final String EMAIL_TEST = "email@test.fr";
    public static final String PROFILE_PICTURE_TEST = "profilePicture.jpg";
    public static final String DESCRIPTION_TEST = "description";
    public static final String UPDATED_USERNAME = "updatedUsername";
    public static final String UPDATED_DESCRIPTION = "updatedDescription";
    public static final String UPDATED_PROFILE_PICTURE = "updatedProfilePicture.jpg";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    private User user;
    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;
    private UUID uuid;
    private UserUpdateDTO userUpdateDTO;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        user = new User();
        userDTO = new UserDTO();
        userCreateDTO = new UserCreateDTO();
        userUpdateDTO = new UserUpdateDTO();

        user.setId(uuid);
        user.setUsername(USERNAME_TEST);
        user.setPassword(PASSWORD_TEST);
        user.setEmail(EMAIL_TEST);
        user.setProfilePicture(PROFILE_PICTURE_TEST);
        user.setDescription(DESCRIPTION_TEST);
        user.setRole(Role.ROLE_USER);
        user.setIsActive(true);

        userDTO.setUsername(USERNAME_TEST);
        userDTO.setEmail(EMAIL_TEST);
        userDTO.setProfilePicture(PROFILE_PICTURE_TEST);
        userDTO.setDescription(DESCRIPTION_TEST);
        userDTO.setRole(Role.ROLE_USER);
        userDTO.setIsActive(true);

        userCreateDTO.setUsername(USERNAME_TEST);
        userCreateDTO.setEmail(EMAIL_TEST);
        userCreateDTO.setPassword(PASSWORD_TEST);
        userCreateDTO.setDescription(DESCRIPTION_TEST);
        userCreateDTO.setProfilePicture(PROFILE_PICTURE_TEST);

        userUpdateDTO.setUsername(UPDATED_USERNAME);
        userUpdateDTO.setDescription(UPDATED_DESCRIPTION);
        userUpdateDTO.setProfilePicture(UPDATED_PROFILE_PICTURE);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(uuid);

        assertNotNull(result);
        assertEquals(USERNAME_TEST, result.getUsername());
        assertEquals(EMAIL_TEST, result.getEmail());
        assertEquals(DESCRIPTION_TEST, result.getDescription());
        assertEquals(PROFILE_PICTURE_TEST, result.getProfilePicture());
        assertEquals(Role.ROLE_USER, result.getRole());
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
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userCreateDTO);

        assertNotNull(createdUser);
        assertEquals(USERNAME_TEST, createdUser.getUsername());
        assertEquals(EMAIL_TEST, createdUser.getEmail());
        assertEquals(DESCRIPTION_TEST, createdUser.getDescription());
        assertEquals(PROFILE_PICTURE_TEST, createdUser.getProfilePicture());
        assertEquals(Role.ROLE_USER, createdUser.getRole());
        assertTrue(createdUser.getIsActive());

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(uuid, userUpdateDTO);

        assertNotNull(updatedUser);
        assertEquals(UPDATED_USERNAME, updatedUser.getUsername());
        assertEquals(UPDATED_DESCRIPTION, updatedUser.getDescription());
        assertEquals(UPDATED_PROFILE_PICTURE, updatedUser.getProfilePicture());

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(uuid, userUpdateDTO));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(uuid));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).delete(user);
    }
}
