package com.spotify.game.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_histories")
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playlist_name", nullable = false)
    private String playlistName;

    @Column(name = "game_date", nullable = false)
    private LocalDateTime gameDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_history",
            joinColumns = {@JoinColumn(name = "game_history_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> users;

    @OneToMany(mappedBy = "gameHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Score> scores;
}
