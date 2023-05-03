package com.spotify.game.rest;

import com.spotify.game.model.entity.User;
import com.spotify.game.properties.AppProperties;
import com.spotify.game.service.EmailService;
import com.spotify.game.service.UserService;
import com.spotify.game.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.spotify.game.helper.EmailType.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("${apiPrefix}/email")
@AllArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;
    private final UserService userService;
    private final EmailService emailService;
    private final AppProperties appProperties;

    @GetMapping("/register-email")
    public ResponseEntity<String> verifyRegisterEmail(@RequestParam("token") String token) {
        String locationUrl = appProperties.getVerification().getLocation();

        try {
            verificationService.verifyToken(token, REGISTER);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", locationUrl);
            return new ResponseEntity<>(headers, FOUND);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/password-email")
    public ResponseEntity<String> verifyPasswordEmail(@RequestParam("token") String token) {
        String locationUrl = appProperties.getUserPassword().getLocation();

        try {
            verificationService.verifyToken(token, PASSWORD);
            URI redirectUri = UriComponentsBuilder.fromUriString(locationUrl + token).build().toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectUri.toString());
            return new ResponseEntity<>(headers, FOUND);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/resend-register-email")
    @ResponseStatus(ACCEPTED)
    public void resendRegisterEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.getUserByEmail(email);
        user.ifPresent(u -> emailService.sendEmail(u, REGISTER));
    }

    @PostMapping("/send-password-email")
    @ResponseStatus(ACCEPTED)
    public void sendPasswordEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.getUserByEmail(email);
        user.ifPresent(u -> emailService.sendEmail(u, PASSWORD));
    }
}