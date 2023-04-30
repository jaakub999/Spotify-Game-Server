package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.exception.ExceptionCode;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.UserRepository;
import com.spotify.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
            throw new SgRuntimeException(ExceptionCode.E009);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setVerified(false);
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPasswordHash(encryptedPassword);

        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) throws SgRuntimeException {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new SgRuntimeException(ExceptionCode.E001)));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new SgRuntimeException(ExceptionCode.E001)));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new SgRuntimeException(ExceptionCode.E001)));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        Optional<User> user = getUserByUsername(username);

        if (user.isPresent()) {
            User userEntity = user.get();

            if (!passwordEncoder.matches(currentPassword, userEntity.getPasswordHash()))
                throw new IllegalArgumentException("Current password is incorrect");

            userEntity.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
        }
    }

    private void handleExistingUser(Optional<User> existingUser, String value) {
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.isVerified()) {
                throw new SgRuntimeException(ExceptionCode.E008);
            } else {
                userRepository.deleteByUsername(value);
                userRepository.deleteByEmail(value);
            }
        }
    }
}