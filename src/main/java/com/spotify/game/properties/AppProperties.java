package com.spotify.game.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private Verification verification;
    private Verification userPassword;
    private Jwt jwt;

    @Data
    public static class Verification {
        private String url;
        private String location;
    }

    @Data
    public static class Jwt {
        private String secret;

        public byte[] getSecretAsBytes() {
            return secret.getBytes();
        }
    }
}