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
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private final SpotifyProperties spotifyProperties;

    @Autowired
    public SpotifyServiceImpl(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
    }

    @Override
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

    @Override
    public List<PlaylistTrack> getPlaylistTracksById(String id, int number) {
        try {
            SpotifyApi spotifyApi = getSpotifyApi();
            setAccessToken(spotifyApi);
            GetPlaylistRequest request = spotifyApi.getPlaylist(id).build();
            Playlist playlist = request.execute();
            return getRandomTracks(playlist, number);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }

        return null;
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
        Paging<PlaylistSimplified> playlistsPaging = request.execute();
        List<PlaylistSimplified> playlists = List.of(playlistsPaging.getItems());
        List<String> names = playlists.stream()
                .map(PlaylistSimplified::getName)
                .toList();

        return playlists;
    }

    private List<PlaylistTrack> getRandomTracks(Playlist playlist, int number) {
        Paging<PlaylistTrack> playlistTracks = playlist.getTracks();
        List<PlaylistTrack> allTracks = new ArrayList<>(List.of(playlistTracks.getItems()));
        List<PlaylistTrack> randomTracks = new ArrayList<>();
        Random random = new Random();
        var size = allTracks.size();

        for (var i = 0; i < number && i < size; i++) {
            var randomIndex = random.nextInt(size);
            randomTracks.add(allTracks.get(randomIndex));
        }

        return randomTracks;
    }
}