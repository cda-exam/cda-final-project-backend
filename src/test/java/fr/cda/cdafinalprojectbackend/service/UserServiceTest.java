package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.dto.user.UserCreateDTO;
import fr.cda.cdafinalprojectbackend.dto.user.UserDTO;
import fr.cda.cdafinalprojectbackend.entity.User;
import fr.cda.cdafinalprojectbackend.exception.UserAlreadyExistsException;
import fr.cda.cdafinalprojectbackend.exception.UserNotFoundException;
import fr.cda.cdafinalprojectbackend.mapper.UserMapper;
import fr.cda.cdafinalprojectbackend.repository.UserRepository;
import fr.cda.cdafinalprojectbackend.security.Role;
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
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws UserNotFoundException {
        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setUsername("usernameTest");
        user.setPassword("passwordTest");
        user.setEmail("email@test");
        user.setProfilePicture("profilePictureTest");
        user.setDescription("descriptionTest");
        user.setRole(Role.ROLE_USER);
        user.setIsActive(true);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("usernameTest");
        userDTO.setEmail("email@test");
        userDTO.setProfilePicture("profilePictureTest");
        userDTO.setDescription("descriptionTest");
        userDTO.setRole(Role.ROLE_USER);
        userDTO.setIsActive(true);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(uuid);

        assertNotNull(result);
        assertEquals("usernameTest", result.getUsername());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(uuid);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() throws UserAlreadyExistsException {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setEmail("test@example.com");
        // Arrange
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));

        // Verify that save() is never called
        verify(userRepository, never()).save(any(User.class));
    }
}
