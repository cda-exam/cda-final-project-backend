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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DBUserServiceTest {

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
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    RoleRepository roleRepository;

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
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userCreateDTO)).thenReturn(user);
        when(userRepository.save(any(DBUser.class))).thenReturn(user);
        when(bCryptPasswordEncoder.encode(userCreateDTO.getPassword())).thenReturn(PASSWORD_TEST);
        when(roleRepository.findRoleByType(RoleEnum.USER)).thenReturn(Optional.of(role));


        DBUser createdDBUser = userService.createUser(userCreateDTO);

        assertNotNull(createdDBUser);
        assertEquals(NICKNAME_TEST, createdDBUser.getNickname());
        assertEquals(EMAIL_TEST, createdDBUser.getEmail());
        assertEquals(DESCRIPTION_TEST, createdDBUser.getDescription());
        assertEquals(PROFILE_PICTURE_TEST, createdDBUser.getProfilePicture());
        assertEquals(RoleEnum.USER, createdDBUser.getRole().getType());
        assertTrue(createdDBUser.getIsActive());

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, times(1)).save(any(DBUser.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);
        when(bCryptPasswordEncoder.encode(userCreateDTO.getPassword())).thenReturn(PASSWORD_TEST);
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));

        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() throws UserNotFoundException {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(any(DBUser.class))).thenReturn(user);

        DBUser updatedDBUser = userService.updateUser(uuid, userUpdateDTO);

        assertNotNull(updatedDBUser);
        assertEquals(UPDATED_NICKNAME, updatedDBUser.getNickname());
        assertEquals(UPDATED_DESCRIPTION, updatedDBUser.getDescription());
        assertEquals(UPDATED_PROFILE_PICTURE, updatedDBUser.getProfilePicture());

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).save(any(DBUser.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(uuid, userUpdateDTO));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).save(any(DBUser.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(uuid));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).delete(any(DBUser.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(uuid));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).delete(user);
    }
}
