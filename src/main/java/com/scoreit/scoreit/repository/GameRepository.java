package com.scoreit.scoreit.repository;

import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByStatus(GameStatus status);
}

