package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Validation;
import fr.cda.cdafinalprojectbackend.repository.ValidationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceImplTest {

    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_CODE = "123456";

    @Mock
    private ValidationRepository validationRepository;

    @Mock
    private NotificationServiceImpl notificationService;

    @InjectMocks
    private ValidationServiceImpl validationService;

    private DBUser testUser;
    private Validation testValidation;

    @BeforeEach
    void setUp() {
        testUser = new DBUser();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail(TEST_EMAIL);

        testValidation = new Validation();
        testValidation.setCode(TEST_CODE);
        testValidation.setUser(testUser);
        testValidation.setCreation(Instant.now());
        testValidation.setExpiration(Instant.now().plusSeconds(600)); // 10 minutes
    }

    @Test
    void saveValidation_ShouldCreateValidationWithCorrectParameters() {
        // Arrange
        ArgumentCaptor<Validation> validationCaptor = ArgumentCaptor.forClass(Validation.class);
        when(validationRepository.save(any(Validation.class))).thenReturn(new Validation());
        doNothing().when(notificationService).sendNotification(any(Validation.class));

        // Act
        validationService.saveValidation(testUser);

        // Assert
        verify(validationRepository).save(validationCaptor.capture());
        
        Validation capturedValidation = validationCaptor.getValue();
        assertNotNull(capturedValidation);
        assertEquals(testUser, capturedValidation.getUser());
        assertNotNull(capturedValidation.getCode());
        assertEquals(6, capturedValidation.getCode().length());
        assertTrue(capturedValidation.getCode().matches("\\d{6}"));

        assertNotNull(capturedValidation.getCreation());
        assertNotNull(capturedValidation.getExpiration());
        assertTrue(capturedValidation.getExpiration().isAfter(capturedValidation.getCreation()));
        assertTrue(ChronoUnit.MINUTES.between(capturedValidation.getCreation(), capturedValidation.getExpiration()) >= 9);
    }

    @Test
    void getValidationByCode_ShouldReturnValidation_WhenCodeExists() {
        // Arrange
        when(validationRepository.findByCode(TEST_CODE)).thenReturn(Optional.of(testValidation));

        // Act
        Validation result = validationService.getValidationByCode(TEST_CODE);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_CODE, result.getCode());
        assertEquals(testUser, result.getUser());
        verify(validationRepository).findByCode(TEST_CODE);
    }

    @Test
    void getValidationByCode_ShouldThrowException_WhenCodeDoesNotExist() {
        // Arrange
        String invalidCode = "000000";
        when(validationRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> validationService.getValidationByCode(invalidCode));
        assertEquals("Invalid code", exception.getMessage());
        verify(validationRepository).findByCode(invalidCode);
    }

    @Test
    void saveValidation_ShouldSendNotification() {
        // Arrange
        when(validationRepository.save(any(Validation.class))).thenReturn(new Validation());
        ArgumentCaptor<Validation> notificationCaptor = ArgumentCaptor.forClass(Validation.class);

        // Act
        validationService.saveValidation(testUser);

        // Assert
        verify(notificationService).sendNotification(notificationCaptor.capture());
        Validation capturedValidation = notificationCaptor.getValue();
        assertNotNull(capturedValidation);
        assertEquals(testUser, capturedValidation.getUser());
    }
}
