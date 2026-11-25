package com.scoreit.scoreit.dto;

import com.scoreit.scoreit.model.Bet;

import java.time.Instant;

public record BetResponse(
        Long id,
        Long matchId,
        String username,
        int predictedHomeScore,
        int predictedAwayScore,
        Integer pointsEarned,
        Instant createdAt

) {
    public static BetResponse from(Bet bet){
        return new BetResponse(
                bet.getId(),
                bet.getMatch().getId(),
                bet.getParticipant().getUser().getUsername(),
                bet.getPredictedHomeScore(),
                bet.getPredictedAwayScore(),
                bet.getPointsEarned(),
                bet.getCreatedAt()
        );
    }
}
