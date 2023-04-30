package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionDTO {
    private String host;
    private boolean started;
    private String code;
    private List<Long> playerIds;
}