package com.scoreit.scoreit.controller.web.view;

import com.scoreit.scoreit.dto.BetResponse;
import com.scoreit.scoreit.dto.MatchResponse;

import java.util.List;

public record MatchView(
        MatchResponse match,
        MatchState state,
        String startLabel,
        BetResponse myGuess,
        List<BetResponse> bets
) {
    public boolean upcoming() {
        return state == MatchState.UPCOMING;
    }

    public boolean live() {
        return state == MatchState.LIVE;
    }

    public boolean finished() {
        return state == MatchState.FINAL;
    }

    public boolean homeWon() {
        return finished() && match.homeScore() > match.awayScore();
    }

    public boolean awayWon() {
        return finished() && match.awayScore() > match.homeScore();
    }
}
