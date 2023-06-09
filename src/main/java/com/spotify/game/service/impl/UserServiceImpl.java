package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.UserRepository;
import com.spotify.game.service.UserService;
import com.spotify.game.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.spotify.game.exception.ExceptionCode.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
    }

    @Override
    @Transactional
    public User createUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            Optional<User> existingUser = userRepository.findByUsername(username);
            handleExistingUser(existingUser, username);
        }

        if (userRepository.existsByEmail(email)) {
            Optional<User> existingUser = userRepository.findByEmail(email);
            handleExistingUser(existingUser, email);
        }

        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(regex)) {
            throw new SgRuntimeException(E009);
        }

        String encryptedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .username(username)
                .email(email)
                .verified(false)
                .passwordHash(encryptedPassword)
                .build();

        userRepository.save(user);

        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) throws SgRuntimeException {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new SgRuntimeException(E001)));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new SgRuntimeException(E001)));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new SgRuntimeException(E001)));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(String token, String newPassword, String confirmNewPassword) {
        if (!confirmNewPassword.equals(newPassword))
            throw new SgRuntimeException(E010);

        String email = verificationService.getEmailByToken(token);
        Optional<User> user = getUserByEmail(email);

        if (user.isPresent()) {
            User userEntity = user.get();
            userEntity.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        }


    }

    private void handleExistingUser(Optional<User> existingUser, String value) {
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getVerified()) {
                throw new SgRuntimeException(E008);
            } else {
                userRepository.deleteByUsername(value);
                userRepository.deleteByEmail(value);
            }
        }
    }
}