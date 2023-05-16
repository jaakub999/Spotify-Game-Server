package com.spotify.game.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "track_groups")
public class TrackGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "trackGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Size(max = 4)
    private List<InternalTrack> tracks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
}
