package com.scoreit.scoreit.controller.web;

import com.scoreit.scoreit.controller.web.view.MatchState;
import com.scoreit.scoreit.controller.web.view.MatchView;
import com.scoreit.scoreit.dto.BetResponse;
import com.scoreit.scoreit.dto.CreateGameRequest;
import com.scoreit.scoreit.dto.CreateMatchRequest;
import com.scoreit.scoreit.dto.GameResponse;
import com.scoreit.scoreit.dto.JoinGameRequest;
import com.scoreit.scoreit.dto.LeaderboardEntryResponse;
import com.scoreit.scoreit.dto.MatchResponse;
import com.scoreit.scoreit.dto.UserResponse;
import com.scoreit.scoreit.service.BetService;
import com.scoreit.scoreit.service.GameService;
import com.scoreit.scoreit.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class GameWebController {

    private static final DateTimeFormatter START_FORMAT =
            DateTimeFormatter.ofPattern("EEE d MMM, HH:mm", Locale.ENGLISH);

    private final GameService gameService;
    private final MatchService matchService;
    private final BetService betService;
    private final CurrentPlayer currentPlayer;
    private final FormValidator formValidator;

    @GetMapping("/games")
    public String games(Model model) {
        model.addAttribute("games", gameService.getGames());
        model.addAttribute("game", null);
        return "game";
    }

    @GetMapping("/games/{id}")
    public String game(@PathVariable Long id, Model model) {
        GameResponse game = gameService.getGameById(id);
        List<LeaderboardEntryResponse> standings = gameService.getLeaderboard(id);
        UserResponse me = (UserResponse) model.getAttribute("me");

        boolean joined = me != null && standings.stream()
                .anyMatch(entry -> entry.username().equals(me.username()));

        List<MatchView> matches = matchService.getMatches(id).stream()
                .map(match -> toView(match, me))
                .toList();

        model.addAttribute("games", gameService.getGames());
        model.addAttribute("game", game);
        model.addAttribute("standings", standings);
        model.addAttribute("joined", joined);
        model.addAttribute("matches", matches);
        return "game";
    }

    @PostMapping("/games")
    public String createGame(@RequestParam(defaultValue = "") String name, RedirectAttributes redirect) {
        CreateGameRequest form = formValidator.validate(
                new CreateGameRequest(name, currentPlayer.requireUserId()));
        GameResponse game = gameService.createGame(form);

        redirect.addFlashAttribute("message", game.name() + " started");
        return "redirect:/games/" + game.id();
    }

    @PostMapping("/games/{id}/join")
    public String joinGame(@PathVariable Long id, RedirectAttributes redirect) {
        gameService.joinGame(id, new JoinGameRequest(currentPlayer.requireUserId()));

        redirect.addFlashAttribute("message", "You joined the game");
        return "redirect:/games/" + id;
    }

    @PostMapping("/games/{id}/matches")
    public String addMatch(@PathVariable Long id,
                           @RequestParam(defaultValue = "") String homeTeam,
                           @RequestParam(defaultValue = "") String awayTeam,
                           @RequestParam(required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                           RedirectAttributes redirect) {
        // The browser sends local time without a zone; the app runs on the player's machine,
        // so the server's zone is the player's zone.
        Instant start = startTime == null ? null : startTime.atZone(ZoneId.systemDefault()).toInstant();

        CreateMatchRequest form = formValidator.validate(new CreateMatchRequest(homeTeam, awayTeam, start));
        MatchResponse match = matchService.createMatch(id, form);

        redirect.addFlashAttribute("message", "Match added");
        return "redirect:/games/" + id + "#match-" + match.id();
    }

    private MatchView toView(MatchResponse match, UserResponse me) {
        MatchState state;
        if (match.homeScore() != null) {
            state = MatchState.FINAL;
        } else if (Instant.now().isBefore(match.startTime())) {
            state = MatchState.UPCOMING;
        } else {
            state = MatchState.LIVE;
        }

        BetResponse myGuess = me == null
                ? null
                : betService.findUserBet(match.id(), me.id()).orElse(null);

        // Other players' guesses stay hidden until the match starts
        List<BetResponse> bets = state == MatchState.UPCOMING
                ? List.of()
                : betService.getMatchBets(match.id()).stream()
                .sorted(Comparator.comparing(BetResponse::pointsEarned,
                        Comparator.nullsLast(Comparator.<Integer>reverseOrder())))
                .toList();

        String startLabel = START_FORMAT.format(match.startTime().atZone(ZoneId.systemDefault()));

        return new MatchView(match, state, startLabel, myGuess, bets);
    }
}

