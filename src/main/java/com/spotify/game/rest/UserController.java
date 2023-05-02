package com.spotify.game.rest;

import com.spotify.game.exception.ExceptionCode;
import com.spotify.game.exception.SgRuntimeException;
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

import static com.spotify.game.helper.EmailType.*;
import static org.springframework.http.HttpStatus.ACCEPTED;

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
    @ResponseStatus(ACCEPTED)
    public void registerUser(@RequestBody UserDTO dto) {
        User user = userService.createUser(dto.getUsername(),
                dto.getPassword(),
                dto.getEmail());
        emailService.sendEmail(user, REGISTER);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();
        String confirmNewPassword = request.getConfirmNewPassword();
        userService.changePassword(token, newPassword, confirmNewPassword);
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
}
