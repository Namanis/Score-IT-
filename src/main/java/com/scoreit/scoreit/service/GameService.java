package com.scoreit.scoreit.service;

import com.scoreit.scoreit.dto.*;
import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.GameParticipant;
import com.scoreit.scoreit.model.User;
import com.scoreit.scoreit.model.enums.GameStatus;
import com.scoreit.scoreit.repository.GameParticipantRepository;
import com.scoreit.scoreit.repository.GameRepository;
import com.scoreit.scoreit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final String GAME_NOT_EXIST = "Game does not exist";

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameParticipantRepository gameParticipantRepository;

    @Transactional
    public GameResponse createGame(CreateGameRequest createGameRequest){
        User creator = userRepository.findById(createGameRequest.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        Game game = new Game();
        game.setName(createGameRequest.name());
        game.setCreatedBy(creator);

        return GameResponse.from(gameRepository.save(game));
    }

    @Transactional(readOnly = true)
    public List<GameResponse> getGames(){
        return gameRepository.findAll().stream().
                map(GameResponse::from).
                filter(game -> game.status() == GameStatus.OPEN).
                toList();
    }

    @Transactional(readOnly = true)
    public GameResponse getGameById(Long id){
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_EXIST));
        return GameResponse.from(game);
    }

    @Transactional
    public void joinGame(Long gameId, JoinGameRequest request){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_EXIST));

        if(game.getStatus() != GameStatus.OPEN){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is not open for joining");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        if (gameParticipantRepository.existsByUser_IdAndGame_Id(user.getId(), gameId)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already joined this game");
        }
        GameParticipant participant = new GameParticipant();
        participant.setUser(user);
        participant.setGame(game);
        gameParticipantRepository.save(participant);

    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard(Long id){
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, GAME_NOT_EXIST));

        return gameParticipantRepository.findByGame_IdOrderByScoreDesc(game.getId()).stream()
                .map(LeaderboardEntryResponse::from)
                .toList();
    }
}
