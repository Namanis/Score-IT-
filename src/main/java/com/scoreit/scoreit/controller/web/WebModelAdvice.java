package com.scoreit.scoreit.controller.web;

import com.scoreit.scoreit.dto.UserResponse;
import com.scoreit.scoreit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Adds the player list and the current player to every web page.
 * Applies only to controllers in this package, not to the REST API.
 */
@ControllerAdvice(basePackageClasses = WebModelAdvice.class)
@RequiredArgsConstructor
public class WebModelAdvice {

    private final UserService userService;
    private final CurrentPlayer currentPlayer;

    @ModelAttribute
    public void addPlayers(Model model) {
        List<UserResponse> players = userService.getUsers();

        UserResponse me = players.stream()
                .filter(user -> user.id().equals(currentPlayer.getUserId()))
                .findFirst()
                .orElse(players.isEmpty() ? null : players.get(0));

        currentPlayer.setUserId(me == null ? null : me.id());

        model.addAttribute("players", players);
        model.addAttribute("me", me);
    }
}
