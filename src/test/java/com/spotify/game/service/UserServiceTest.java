package com.spotify.game.service;

import com.spotify.game.model.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUserTest() {
        User user = userService.createUser("test_username", "test_password", "test@example.com");

        assertNotNull(user.getId());
        assertEquals("test_username", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertFalse(user.isVerified());
    }

    @Test
    public void changePasswordTest() {
        User user = userService.createUser("test_username", "test_password", "test@example.com");
        userService.changePassword(user.getUsername(), "test_password", "new_password");
        user = userService.getUserByUsername(user.getUsername()).get();

        assertTrue(passwordEncoder.matches("new_password", user.getPasswordHash()));
    }
}
