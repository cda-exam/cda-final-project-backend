package fr.cda.cdafinalprojectbackend.configuration.security;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Role;
import fr.cda.cdafinalprojectbackend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_NICKNAME = "TestUser";

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private JwtService jwtService;

    private DBUser mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        mockRole = new Role();
        mockRole.setType(RoleEnum.USER);

        mockUser = new DBUser();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail(TEST_EMAIL);
        mockUser.setNickname(TEST_NICKNAME);
        mockUser.setRole(mockRole);
        mockUser.setIsActive(true);
    }

    @Test
    void getJwtToken_ShouldReturnValidToken() {
        // Arrange
        when(userService.loadUserByUsername(TEST_EMAIL)).thenReturn(mockUser);

        // Act
        Map<String, String> tokenMap = jwtService.getJwtToken(TEST_EMAIL);

        // Assert
        assertNotNull(tokenMap);
        assertTrue(tokenMap.containsKey("bearer"));
        String token = tokenMap.get("bearer");
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(userService).loadUserByUsername(TEST_EMAIL);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        when(userService.loadUserByUsername(TEST_EMAIL)).thenReturn(mockUser);
        Map<String, String> tokenMap = jwtService.getJwtToken(TEST_EMAIL);
        String token = tokenMap.get("bearer");

        // Act
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertEquals(TEST_EMAIL, extractedUsername);
    }

    @Test
    void isTokenExpired_ShouldReturnFalseForValidToken() {
        // Arrange
        when(userService.loadUserByUsername(TEST_EMAIL)).thenReturn(mockUser);
        Map<String, String> tokenMap = jwtService.getJwtToken(TEST_EMAIL);
        String token = tokenMap.get("bearer");

        // Act
        boolean isExpired = jwtService.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_ShouldThrowExceptionForInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> jwtService.isTokenExpired(invalidToken));
        assertNotNull(exception);
    }

    @Test
    void getJwtToken_ShouldIncludeCorrectClaims() {
        // Arrange
        when(userService.loadUserByUsername(TEST_EMAIL)).thenReturn(mockUser);

        // Act
        Map<String, String> tokenMap = jwtService.getJwtToken(TEST_EMAIL);
        String token = tokenMap.get("bearer");

        // Assert
        assertNotNull(token);
        String username = jwtService.extractUsername(token);
        assertEquals(TEST_EMAIL, username);
    }
}
