package com.scoreit.scoreit.controller.api;

import com.scoreit.scoreit.dto.BetResponse;
import com.scoreit.scoreit.dto.PlaceBetRequest;
import com.scoreit.scoreit.service.BetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class BetController {

    private final BetService betService;

    @PostMapping("/{matchId}/bets")
    @ResponseStatus(HttpStatus.CREATED)
    public BetResponse placeBet(@PathVariable Long matchId, @Valid @RequestBody PlaceBetRequest placeBetRequest){
        return betService.placeBet(matchId, placeBetRequest);
    }

    @GetMapping("/{matchId}/bets")
    public List<BetResponse> getMatchBets(@PathVariable Long matchId){
        return betService.getMatchBets(matchId);
    }


}
