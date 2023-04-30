package com.spotify.game.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "verified", nullable = false)
    private boolean verified;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_history",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "game_history_id")}
    )
    private List<GameHistory> gameHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Score> scores;
}