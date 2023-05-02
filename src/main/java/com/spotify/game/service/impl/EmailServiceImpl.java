package com.spotify.game.service.impl;

import com.spotify.game.helper.EmailType;
import com.spotify.game.model.entity.User;
import com.spotify.game.model.entity.VerificationToken;
import com.spotify.game.properties.AppProperties;
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
    private final AppProperties appProperties;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender,
                            VerificationService verificationService,
                            AppProperties appProperties) {

        this.javaMailSender = javaMailSender;
        this.verificationService = verificationService;
        this.appProperties = appProperties;
    }

    @Override
    public void sendEmail(User user, EmailType type) {
        new Thread(() -> {
            String email = user.getEmail();
            VerificationToken verificationToken = verificationService.generateVerificationToken(email);
            String htmlBody = createBody(user.getUsername(), verificationToken.getToken(), type);

            try {
                MimeMessage message = generateMessage(email, type.getSubject(), htmlBody);
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String createBody(String username, String token, EmailType type) {
        String verificationUrl;

        if (type == EmailType.REGISTER) {
            verificationUrl = appProperties.getVerification().getUrl();

            return "<html><body><h2>Hello " + username + ",</h2>" +
                    "<p>Please click on the link below to verify your email address:</p>" +
                    "<a href='" + verificationUrl + "?token=" + token + "'>Verify your email address</a>" +
                    "<p>If you did not request this verification, please ignore this email.</p>";
        }

        else if (type == EmailType.PASSWORD) {
            verificationUrl = appProperties.getUserPassword().getUrl();

            return "<html><body><h2>Hello " + username + ",</h2>" +
                    "<p>Please click on the link below to reset your password:</p>" +
                    "<a href='" + verificationUrl + "?token=" + token + "'>Reset Password</a>" +
                    "<p>If you did not request changing your password, please ignore this email.</p>";
        }

        return null;
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
