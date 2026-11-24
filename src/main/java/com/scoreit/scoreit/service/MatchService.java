package com.scoreit.scoreit.service;


import com.scoreit.scoreit.dto.CreateMatchRequest;
import com.scoreit.scoreit.dto.MatchResponse;
import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.Match;
import com.scoreit.scoreit.model.enums.GameStatus;
import com.scoreit.scoreit.repository.GameRepository;
import com.scoreit.scoreit.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final GameRepository gameRepository;

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
}
