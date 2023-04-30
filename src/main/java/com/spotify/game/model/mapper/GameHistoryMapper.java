package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.GameHistoryDTO;
import com.spotify.game.model.entity.GameHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameHistoryMapper {
    public static GameHistoryDTO mapGameHistoryToDto(GameHistory source) {
        return GameHistoryDTO.builder()
                .playlistName(source.getPlaylistName())
                .gameDate(source.getGameDate())
                .users(source.getUsers())
                .scores(source.getScores())
                .build();
    }

    public static List<GameHistoryDTO> mapGameHistoryListToDto(List<GameHistory> source) {
        List<GameHistoryDTO> gameHistoryDTOs = new ArrayList<>();

        for (GameHistory gameHistory : source) {
            GameHistoryDTO dto = mapGameHistoryToDto(gameHistory);
            gameHistoryDTOs.add(dto);
        }

        return gameHistoryDTOs;
    }
}
