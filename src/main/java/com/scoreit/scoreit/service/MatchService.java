package com.scoreit.scoreit.service;


import com.scoreit.scoreit.dto.CreateMatchRequest;
import com.scoreit.scoreit.dto.MatchResponse;
import com.scoreit.scoreit.dto.MatchResultRequest;
import com.scoreit.scoreit.model.Bet;
import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.GameParticipant;
import com.scoreit.scoreit.model.Match;
import com.scoreit.scoreit.model.enums.GameStatus;
import com.scoreit.scoreit.repository.BetRepository;
import com.scoreit.scoreit.repository.GameRepository;
import com.scoreit.scoreit.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final GameRepository gameRepository;
    private final BetRepository betRepository;
    private final PointsCalculator pointsCalculator;

    @Transactional
    public MatchResponse createMatch(Long gameId, CreateMatchRequest createMatchRequest){
        String homeTeam = createMatchRequest.homeTeam().trim();
        String awayTeam = createMatchRequest.awayTeam().trim();

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist"));

        if (game.getStatus() == GameStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot add matches to a finished game");
        }
        if (homeTeam.equalsIgnoreCase(awayTeam)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Teams must be different");
        }

        Match match = new Match();
        match.setGame(game);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setStartTime(createMatchRequest.startTime());

        Match savedMatch = matchRepository.save(match);
        return MatchResponse.from(savedMatch);
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> getMatches(Long gameId){
        if (!gameRepository.existsById(gameId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist");
        }

        return matchRepository.findByGame_IdOrderByStartTime(gameId).stream()
                .map(MatchResponse::from)
                .toList();
    }

    @Transactional
    public MatchResponse finishMatch(Long matchId, MatchResultRequest matchResultRequest){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match does not exist"));

        if (match.getHomeScore() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Match is already finished");
        }
        if (Instant.now().isBefore(match.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match has not started yet");
        }
        if (matchResultRequest.homeScore().equals(matchResultRequest.awayScore())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Basketball matches cannot end in a tie");
        }

        match.setHomeScore(matchResultRequest.homeScore());
        match.setAwayScore(matchResultRequest.awayScore());

        for (Bet bet : betRepository.findByMatch_Id(matchId)) {
            int points = pointsCalculator.calculate(
                    bet.getPredictedHomeScore(), bet.getPredictedAwayScore(),
                    matchResultRequest.homeScore(), matchResultRequest.awayScore());

            bet.setPointsEarned(points);

            GameParticipant participant = bet.getParticipant();
            participant.setScore(participant.getScore() + points);
        }

        return MatchResponse.from(match);
    }
}
