package com.spotify.game.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotNull
    private String token;

    @NotNull
    private String newPassword;

    @NotNull
    private String confirmNewPassword;
}
