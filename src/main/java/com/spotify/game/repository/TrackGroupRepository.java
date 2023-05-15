package com.spotify.game.repository;

import com.spotify.game.model.entity.TrackGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackGroupRepository extends JpaRepository<TrackGroup, Long> {}
