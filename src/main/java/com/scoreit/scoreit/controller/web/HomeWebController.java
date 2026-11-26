package com.scoreit.scoreit.controller.web;

import com.scoreit.scoreit.dto.GameResponse;
import com.scoreit.scoreit.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeWebController {

    private final GameService gameService;

    @GetMapping("/")
    public String home() {
        List<GameResponse> games = gameService.getGames();
        return games.isEmpty() ? "redirect:/games" : "redirect:/games/" + games.get(0).id();
    }
}
