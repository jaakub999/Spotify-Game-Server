package com.spotify.game.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spotify.client")
@Data
public class SpotifyProperties {
    private String id;
    private String secret;
}