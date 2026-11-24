package com.scoreit.scoreit.dto;

import com.scoreit.scoreit.model.GameParticipant;

public record LeaderboardEntryResponse(
        Long userId,
        String username,
        int score
) {
    public static LeaderboardEntryResponse from(GameParticipant gameParticipant) {
        return new LeaderboardEntryResponse(
                gameParticipant.getUser().getId(),
                gameParticipant.getUser().getUsername(),
                gameParticipant.getScore());
    }
}
