package com.spotify.game.rest;

import com.spotify.game.model.mapper.UserMapper;
import com.spotify.game.model.entity.User;
import com.spotify.game.model.dto.UserDTO;
import com.spotify.game.request.ChangePasswordRequest;
import com.spotify.game.service.EmailService;
import com.spotify.game.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${apiPrefix}/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO dto) {
        User user = userService.createUser(dto.getUsername(),
                dto.getPassword(),
                dto.getEmail());
        emailService.sendVerificationEmail(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<?> resendVerificationEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            emailService.sendVerificationEmail(user.get());
            return ResponseEntity.ok("Verification email sent successfully");
        }

        else return ResponseEntity.ok("User not found");
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(UserMapper.mapUsersListToDto(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(UserMapper.mapUserToDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping ("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(u -> ResponseEntity.ok(UserMapper.mapUserToDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(u -> ResponseEntity.ok(UserMapper.mapUserToDto(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody @Valid ChangePasswordRequest request) {
        String username = authentication.getName();

        try {
            userService.changePassword(username, request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
