package com.spotify.game.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SessionRequest {

    @NotNull
    private String session;

    @NotNull
    private String playlist;

    @NotNull
    private Integer tracks;
}
