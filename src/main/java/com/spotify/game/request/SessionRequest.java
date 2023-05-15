package com.spotify.game.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionRequest {

    @NotNull
    private String sessionCode;

    @NotNull
    private String playlistId;

    @NotNull
    private Integer tracksNumber;
}
