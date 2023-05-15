package com.spotify.game.repository;

import com.spotify.game.model.entity.InternalTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalTrackRepository extends JpaRepository<InternalTrack, Long> {}
