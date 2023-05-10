package com.spotify.game.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TracksRequest {

    @NotNull
    private String id;

    @NotNull
    private Integer number;
}
