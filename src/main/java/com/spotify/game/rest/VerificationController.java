package com.spotify.game.rest;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.service.VerificationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/verify-email")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        try {
            verificationService.verifyToken(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "http://localhost:4200");
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (SgRuntimeException e) {
            return ResponseEntity.ok("invalid-token");
        }
    }
}