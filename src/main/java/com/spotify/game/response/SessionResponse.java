package com.spotify.game.response;

import com.spotify.game.model.dto.SessionDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponse {

    @NotNull
    private SessionDTO session;

    @NotNull
    private String username;
}
