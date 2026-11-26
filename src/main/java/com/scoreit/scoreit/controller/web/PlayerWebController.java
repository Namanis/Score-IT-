package com.scoreit.scoreit.controller.web;

import com.scoreit.scoreit.dto.CreateUserRequest;
import com.scoreit.scoreit.dto.UserResponse;
import com.scoreit.scoreit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerWebController {

    private final UserService userService;
    private final CurrentPlayer currentPlayer;
    private final FormValidator formValidator;

    @PostMapping
    public String createPlayer(@RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               HttpServletRequest request,
                               RedirectAttributes redirect) {
        CreateUserRequest form = formValidator.validate(new CreateUserRequest(username, email));
        UserResponse user = userService.createUser(form);

        currentPlayer.setUserId(user.id());
        redirect.addFlashAttribute("message", "Welcome, " + user.username());
        return Redirects.back(request);
    }

    @PostMapping("/select")
    public String selectPlayer(@RequestParam Long userId, HttpServletRequest request) {
        currentPlayer.setUserId(userId);
        return Redirects.back(request);
    }
}
