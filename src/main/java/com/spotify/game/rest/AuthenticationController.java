package com.spotify.game.rest;

import com.spotify.game.model.entity.User;
import com.spotify.game.request.AuthRequest;
import com.spotify.game.response.AuthResponse;
import com.spotify.game.security.JwtTokenProvider;
import com.spotify.game.service.AuthenticationService;
import com.spotify.game.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("${apiPrefix}/login")
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationService authService,
                                    UserService userService,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        String username = request.getUsername();
        String password = URLDecoder.decode(request.getPassword(), StandardCharsets.UTF_8);
        Optional<User> user = userService.getUserByUsername(username);

        if (user.isPresent() && authService.authenticate(username, password)) {
            if (authService.isUserVerified(username)) {
                String token = jwtTokenProvider.generateToken(user.get());
                return ResponseEntity.ok()
                        .header(AUTHORIZATION, "Bearer " + token)
                        .body(new AuthResponse(token));
            }

            else return ResponseEntity.status(FORBIDDEN).body("User is not verified");
        }

        else return ResponseEntity.status(UNAUTHORIZED).body("Invalid username or password");
    }
}