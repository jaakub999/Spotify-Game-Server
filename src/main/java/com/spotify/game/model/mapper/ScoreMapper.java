package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.ScoreDTO;
import com.spotify.game.model.entity.Score;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ScoreMapper {
    public static ScoreDTO mapScoreToDto(Score source) {
        return ScoreDTO.builder()
                .points(source.getPoints())
                .user(source.getUser())
                .gameHistory(source.getGameHistory())
                .build();
    }

    public static List<ScoreDTO> mapScoreListToDto(List<Score> source) {
        List<ScoreDTO> scoreDTOs = new ArrayList<>();

        for (Score score : source) {
            ScoreDTO dto = mapScoreToDto(score);
            scoreDTOs.add(dto);
        }

        return scoreDTOs;
    }
}
