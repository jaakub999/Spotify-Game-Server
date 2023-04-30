package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.UserDTO;
import com.spotify.game.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDTO mapUserToDto(User source) {
        return UserDTO.builder()
                .username(source.getUsername())
                .password(source.getPasswordHash())
                .email(source.getEmail())
                .verified(source.isVerified())
                .sessionId(source.getSession() != null ? source.getSession().getId() : null)
                .gameHistories(source.getGameHistories().isEmpty() ? null : source.getGameHistories())
                .scores(source.getScores().isEmpty() ? null : source.getScores())
                .build();
    }

    public static List<UserDTO> mapUsersListToDto(List<User> source) {
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user: source) {
            UserDTO dto = mapUserToDto(user);
            userDTOs.add(dto);
        }

        return userDTOs;
    }
}
