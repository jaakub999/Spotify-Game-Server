package com.spotify.game.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "playlist_id")
    private String playlistId;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "date")
    private LocalDateTime date;

    @JsonManagedReference
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> players = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrackGroup> trackGroups = new ArrayList<>();
}