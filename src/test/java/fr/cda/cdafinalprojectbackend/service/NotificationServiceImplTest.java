package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_CODE = "123456";

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Validation validation;
    private DBUser user;

    @BeforeEach
    void setUp() {
        user = new DBUser();
        user.setId(UUID.randomUUID());
        user.setEmail(TEST_EMAIL);

        validation = new Validation();
        validation.setUser(user);
        validation.setCode(TEST_CODE);
        validation.setCreation(Instant.now());
        validation.setExpiration(Instant.now().plusSeconds(600));
    }

    @Test
    void sendNotification_ShouldSendEmail() {
        // Act
        notificationService.sendNotification(validation);

        // Assert
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendNotification_ShouldSendEmailWithCorrectParameters() {
        // Arrange
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        notificationService.sendNotification(validation);

        // Assert
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        
        assertEquals("no-reply@cdafinalproject.fr", sentMessage.getFrom());
        assertEquals(TEST_EMAIL, sentMessage.getTo()[0]);
        assertEquals("CODE D'ACCÈS", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(TEST_CODE));
    }
}
