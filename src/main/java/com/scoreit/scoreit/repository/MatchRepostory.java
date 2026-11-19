package com.scoreit.scoreit.repository;

import com.scoreit.scoreit.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepostory extends JpaRepository<Match, Long> {
    List<Match> findByGame_IdOrderByStartTime(Long gameId);
}
