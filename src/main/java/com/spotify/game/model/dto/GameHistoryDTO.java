package com.spotify.game.model.dto;

import com.spotify.game.model.entity.Score;
import com.spotify.game.model.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GameHistoryDTO {
    private String playlistName;
    private LocalDateTime gameDate;
    private List<User> users;
    private List<Score> scores;
}
