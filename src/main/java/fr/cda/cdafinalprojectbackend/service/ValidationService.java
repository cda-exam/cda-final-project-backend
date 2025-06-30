package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.entity.DBUser;
import fr.cda.cdafinalprojectbackend.entity.Validation;
import fr.cda.cdafinalprojectbackend.repository.ValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final ValidationRepository validationRepository;
    private final NotificationService notificationService;

    public void saveValidation(DBUser user) {
        Validation validation = new Validation();

        Instant creation = Instant.now();
        Instant expiration = Instant.now().plus(10, ChronoUnit.MINUTES);

        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setUser(user);
        validation.setCreation(creation);
        validation.setExpiration(expiration);
        validation.setCode(code);

        this.validationRepository.save(validation);
        this.notificationService.sendNotification(validation);
    }

    public Validation getValidationByCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(
                () -> new RuntimeException("Invalid code")
        );
    }
}
