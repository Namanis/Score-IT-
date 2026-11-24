package com.scoreit.scoreit.controller.api;

import com.scoreit.scoreit.dto.CreateMatchRequest;
import com.scoreit.scoreit.dto.MatchResponse;
import com.scoreit.scoreit.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/{gameId}/matches")
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResponse createMatch(@PathVariable Long gameId, @Valid @RequestBody CreateMatchRequest createMatchRequest){
        return matchService.createMatch(gameId, createMatchRequest);
    }

    @GetMapping("/{gameId}/matches")
    public List<MatchResponse> getMatches(@PathVariable Long gameId){
        return matchService.getMatches(gameId);
    }

}
