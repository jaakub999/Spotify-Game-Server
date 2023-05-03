package com.spotify.game.security;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.User;
import com.spotify.game.properties.AppProperties;
import io.jsonwebtoken.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.spotify.game.exception.ExceptionCode.E011;
import static com.spotify.game.exception.ExceptionCode.E012;
import static com.spotify.game.security.SecurityConstants.EXPIRE_DURATION;
import static com.spotify.game.security.SecurityConstants.TOKEN_PREFIX;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtTokenProvider {

    @Autowired
    private AppProperties appProperties;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, appProperties.getJwt().getSecretAsBytes())
                .compact();
    }

    public Claims parseToken(@NotNull String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(appProperties.getJwt().getSecretAsBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new SgRuntimeException(E011, e);
        } catch (SignatureException e) {
            throw new SgRuntimeException(E012, e);
        }
    }
}