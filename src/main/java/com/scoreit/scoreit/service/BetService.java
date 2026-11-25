package com.scoreit.scoreit.service;

import com.scoreit.scoreit.dto.BetResponse;
import com.scoreit.scoreit.dto.PlaceBetRequest;
import com.scoreit.scoreit.model.Bet;
import com.scoreit.scoreit.model.GameParticipant;
import com.scoreit.scoreit.model.Match;
import com.scoreit.scoreit.repository.BetRepository;
import com.scoreit.scoreit.repository.GameParticipantRepository;
import com.scoreit.scoreit.repository.MatchRepository;
import com.scoreit.scoreit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final GameParticipantRepository gameParticipantRepository;

    @Transactional
    public BetResponse placeBet(Long matchId, PlaceBetRequest placeBetRequest) {
        if (!userRepository.existsById(placeBetRequest.userId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match does not exist"));

        if (!Instant.now().isBefore(match.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match has already started");
        }

        GameParticipant participant = gameParticipantRepository
                .findByUser_IdAndGame_Id(placeBetRequest.userId(), match.getGame().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User has not joined this game"));

        if (betRepository.existsByParticipant_IdAndMatch_Id(participant.getId(), match.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already placed a bet on this match");
        }

        Bet bet = new Bet();
        bet.setPredictedAwayScore(placeBetRequest.predictedAwayScore());
        bet.setPredictedHomeScore(placeBetRequest.predictedHomeScore());
        bet.setMatch(match);
        bet.setParticipant(participant);

        return BetResponse.from(betRepository.save(bet));
    }

    @Transactional(readOnly = true)
    public List<BetResponse> getMatchBets(Long matchId){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match does not exist"));

        if (Instant.now().isBefore(match.getStartTime())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bets are hidden until the match starts");
        }

        return betRepository.findByMatch_Id(matchId).stream()
                .map(BetResponse::from)
                .toList();
    }
}
