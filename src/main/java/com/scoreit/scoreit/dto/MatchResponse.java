package com.scoreit.scoreit.dto;

import com.scoreit.scoreit.model.Match;

import java.time.Instant;

public record MatchResponse(
        Long id,
        String gameName,
        String homeTeam,
        String awayTeam,
        Instant startTime,
        Integer homeScore,
        Integer awayScore
) {
    public static MatchResponse from(Match match){
        return new MatchResponse(
                match.getId(),
                match.getGame().getName(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getStartTime(),
                match.getHomeScore(),
                match.getAwayScore());
    }
}
