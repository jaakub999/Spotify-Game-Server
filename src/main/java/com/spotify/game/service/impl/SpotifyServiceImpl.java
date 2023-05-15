package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.properties.SpotifyProperties;
import com.spotify.game.service.SpotifyService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;

import java.io.IOException;
import java.util.*;

import static com.spotify.game.exception.ExceptionCode.E013;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private final SpotifyProperties spotifyProperties;

    @Autowired
    public SpotifyServiceImpl(SpotifyProperties spotifyProperties) {
        this.spotifyProperties = spotifyProperties;
    }

    @Override
    public List<PlaylistSimplified> getPlaylists(String query) {
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
    public Map<Integer, Map<Track, Boolean>> getPlaylistTracks(String id, int number) {
        try {
            SpotifyApi spotifyApi = getSpotifyApi();
            setAccessToken(spotifyApi);
            GetPlaylistRequest request = spotifyApi.getPlaylist(id).build();
            Playlist playlist = request.execute();
            return getTracks(playlist, number);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new SgRuntimeException(E013);
        }
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

        return List.of(playlistsPaging.getItems());
    }

    private Map<Integer, Map<Track, Boolean>> getTracks(Playlist playlist, int number) {
        List<Track> allTracks = extractAllTracksFromPlaylist(playlist);
        List<Track> randomTracks = getRandomTracks(allTracks, number);
        Map<Integer, Map<Track, Boolean>> tracksMap = new HashMap<>();

        for (var i = 0; i < randomTracks.size(); i++) {
            Track randomTrack = randomTracks.get(i);
            Map<Track, Boolean> innerTracksMap = getRandomFalseTracks(allTracks, randomTrack);
            innerTracksMap.put(randomTrack, true);
            tracksMap.put(i, innerTracksMap);
        }

        return tracksMap;
    }

    private List<Track> extractAllTracksFromPlaylist(Playlist playlist) {
        List<PlaylistTrack> playlistTracks = List.of(playlist.getTracks().getItems());
        List<Track> allTracks = new ArrayList<>();

        for (PlaylistTrack playlistTrack : playlistTracks) {
            if (playlistTrack.getTrack() instanceof Track track)
                allTracks.add(track);
        }

        return allTracks;
    }

    private List<Track> getRandomTracks(List<Track> allTracks, int number) {
        List<Track> randomTracks = new ArrayList<>();
        Random random = new Random();

        for (var i = 0; i < allTracks.size() && i < number; i++) {
            Track track;

            do {
                int randomIndex = random.nextInt(allTracks.size());
                track = allTracks.get(randomIndex);
            } while (randomTracks.contains(track));

            randomTracks.add(track);
        }

        return randomTracks;
    }

    private Map<Track, Boolean> getRandomFalseTracks(List<Track> allTracks, Track randomTrack) {
        Map<Track, Boolean> tracksMap = new HashMap<>();
        final int tracksNumber = 3;
        int tracksCounter = 0;
        Random random = new Random();

        while (tracksCounter < tracksNumber) {
            int randomIndex = random.nextInt(allTracks.size());
            Track track = allTracks.get(randomIndex);

            if (randomTrack == track || tracksMap.containsKey(track))
                continue;

            tracksMap.put(track, false);
            tracksCounter++;
        }

        return tracksMap;
    }
}