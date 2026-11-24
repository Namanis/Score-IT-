package com.scoreit.scoreit.dto;

import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.enums.GameStatus;

import java.time.Instant;

public record GameResponse(
        Long Id,
        String name,
        GameStatus status,
        String createdBy,
        Instant createdAt
) {
    public static GameResponse from(Game game) {
        return new GameResponse(game.getId(),
                game.getName(),
                game.getStatus(),
                game.getCreatedBy().getUsername(),
                game.getCreatedAt());
    }
}
