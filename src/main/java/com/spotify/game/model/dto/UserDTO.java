package com.spotify.game.model.dto;

import com.spotify.game.model.entity.GameHistory;
import com.spotify.game.model.entity.Score;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private boolean verified;
    private Long sessionId;
    private List<GameHistory> gameHistories;
    private List<Score> scores;
}
