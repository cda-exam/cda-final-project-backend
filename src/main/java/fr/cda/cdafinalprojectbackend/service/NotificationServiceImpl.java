package fr.cda.cdafinalprojectbackend.service;

import fr.cda.cdafinalprojectbackend.entity.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl {
    JavaMailSender javaMailSender;
    public void sendNotification(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@cdafinalproject.fr");
        mailMessage.setTo(validation.getUser().getEmail());
        mailMessage.setSubject("CODE D'ACCÈS");
        mailMessage.setText("Votre code d'activation est : " + validation.getCode());

        this.javaMailSender.send(mailMessage);
    }
}
