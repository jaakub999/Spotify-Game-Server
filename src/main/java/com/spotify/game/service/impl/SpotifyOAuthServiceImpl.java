package com.spotify.game.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.properties.SpotifyProperties;
import com.spotify.game.service.SpotifyOAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.spotify.game.exception.ExceptionCode.E014;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@AllArgsConstructor
@Service
public class SpotifyOAuthServiceImpl implements SpotifyOAuthService {

    private final SpotifyProperties properties;

    private final static String TOKEN_URL = "https://accounts.spotify.com/api/token";

    @Override
    public String getAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = createHttpHeaders();
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "client_credentials");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getStatusCode() == OK) {
                String responseBody = responseEntity.getBody();
                JsonNode jsonNode = new ObjectMapper().readTree(responseBody);

                return jsonNode.get("access_token").asText();
            }

            else throw new SgRuntimeException(E014);

        } catch (IOException | RuntimeException e) {
            throw new SgRuntimeException(E014);
        }
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, getEncodedCredentials());

        return headers;
    }

    private String getEncodedCredentials() {
        String clientId = properties.getId();
        String clientSecret = properties.getSecret();
        String credentials = clientId + ":" + clientSecret;

        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
