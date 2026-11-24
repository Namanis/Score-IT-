package com.scoreit.scoreit.controller.api;

import com.scoreit.scoreit.dto.CreateGameRequest;
import com.scoreit.scoreit.dto.GameResponse;
import com.scoreit.scoreit.dto.JoinGameRequest;
import com.scoreit.scoreit.dto.LeaderboardEntryResponse;
import com.scoreit.scoreit.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@Valid @RequestBody CreateGameRequest createGameRequest){
        return gameService.createGame(createGameRequest);
    }

    @GetMapping("/{id}")
    public GameResponse getGameById(@PathVariable Long id){
        return gameService.getGameById(id);
    }

    @GetMapping("")
    public List<GameResponse> getGames(){
        return gameService.getGames();
    }

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinGame(@PathVariable Long id, @Valid @RequestBody JoinGameRequest joinGameRequest){
        gameService.joinGame(id, joinGameRequest);
    }

    @GetMapping("/{id}/leaderboard")
    @ResponseStatus(HttpStatus.FOUND)
    public List<LeaderboardEntryResponse> getLeaderboard(@PathVariable Long id){
        return gameService.getLeaderboard(id);
    }
}