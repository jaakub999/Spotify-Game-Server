package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private Boolean verified;
    private Long sessionId;
}
