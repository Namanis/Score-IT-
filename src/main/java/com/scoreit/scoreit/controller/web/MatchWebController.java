package com.scoreit.scoreit.controller.web;

import com.scoreit.scoreit.dto.MatchResultRequest;
import com.scoreit.scoreit.dto.PlaceBetRequest;
import com.scoreit.scoreit.service.BetService;
import com.scoreit.scoreit.service.MatchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/matches/{matchId}")
@RequiredArgsConstructor
public class MatchWebController {

    private final BetService betService;
    private final MatchService matchService;
    private final CurrentPlayer currentPlayer;
    private final FormValidator formValidator;

    @PostMapping("/bets")
    public String placeBet(@PathVariable Long matchId,
                           @RequestParam(required = false) Integer home,
                           @RequestParam(required = false) Integer away,
                           HttpServletRequest request,
                           RedirectAttributes redirect) {
        PlaceBetRequest form = formValidator.validate(
                new PlaceBetRequest(currentPlayer.requireUserId(), home, away));
        betService.placeBet(matchId, form);

        redirect.addFlashAttribute("message", "Guess saved");
        return Redirects.back(request, "match-" + matchId);
    }

    @PostMapping("/result")
    public String finishMatch(@PathVariable Long matchId,
                              @RequestParam(required = false) Integer home,
                              @RequestParam(required = false) Integer away,
                              HttpServletRequest request,
                              RedirectAttributes redirect) {
        MatchResultRequest form = formValidator.validate(new MatchResultRequest(home, away));
        matchService.finishMatch(matchId, form);

        redirect.addFlashAttribute("message", "Final score saved and points awarded");
        return Redirects.back(request, "match-" + matchId);
    }
}
