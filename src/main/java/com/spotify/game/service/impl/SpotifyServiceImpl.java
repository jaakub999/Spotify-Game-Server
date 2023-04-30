package com.spotify.game.service.impl;

import com.spotify.game.properties.SpotifyProperties;
import com.spotify.game.service.SpotifyService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private final SpotifyProperties spotifyProperties;

    @Autowired
    public SpotifyServiceImpl(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
    }

    public List<PlaylistSimplified> getPlaylistsByName(String query) {
        if (query != null && !query.isEmpty()) {
            try {
                SpotifyApi spotifyApi = getSpotifyApi();
                setAccessToken(spotifyApi);
                return searchPlaylists(spotifyApi, query);
            } catch (IOException | SpotifyWebApiException | ParseException e) {
                e.printStackTrace();
            }
        }

        return Collections.emptyList();
    }

    private SpotifyApi getSpotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(spotifyProperties.getId())
                .setClientSecret(spotifyProperties.getSecret())
                .build();
    }

    private void setAccessToken(SpotifyApi spotifyApi) throws IOException, SpotifyWebApiException, ParseException {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        ClientCredentials clientCredentials = clientCredentialsRequest.execute();
        spotifyApi.setAccessToken(clientCredentials.getAccessToken());
    }

    private List<PlaylistSimplified> searchPlaylists(SpotifyApi spotifyApi, String query) throws IOException, SpotifyWebApiException, ParseException {
        SearchPlaylistsRequest request = spotifyApi.searchPlaylists(query).build();
        Paging<PlaylistSimplified> playlists = request.execute();
        return List.of(playlists.getItems());
    }
}