package com.spotify.game.service.impl;

import com.spotify.game.model.entity.User;
import com.spotify.game.model.entity.VerificationToken;
import com.spotify.game.service.EmailService;
import com.spotify.game.service.VerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final VerificationService verificationService;

    @Value("${app.verification.url}")
    private String verificationUrl;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender,
                            VerificationService verificationService) {

        this.javaMailSender = javaMailSender;
        this.verificationService = verificationService;
    }

    @Override
    public void sendVerificationEmail(User user) {
        new Thread(() -> {
            String email = user.getEmail();
            VerificationToken verificationToken = verificationService.generateVerificationToken(email);
            String subject = "Verify your email address";
            String htmlBody = "<html><body><h2>Hello " + user.getUsername() + ",</h2>" +
                    "<p>Please click on the link below to verify your email address:</p>" +
                    "<a href='" + verificationUrl + "?token=" + verificationToken.getToken() + "'>Verify your email address</a>" +
                    "<p>If you did not request this verification, please ignore this email.</p>";
            try {
                MimeMessage message = generateMessage(email, subject, htmlBody);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private MimeMessage generateMessage(String email, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        return message;
    }
}
