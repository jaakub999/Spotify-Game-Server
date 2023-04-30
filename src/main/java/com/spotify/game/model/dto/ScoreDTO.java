package com.spotify.game.model.dto;

import com.spotify.game.model.entity.GameHistory;
import com.spotify.game.model.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreDTO {
    private Integer points;
    private User user;
    private GameHistory gameHistory;
}