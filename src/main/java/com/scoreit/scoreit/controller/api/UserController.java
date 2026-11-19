package com.scoreit.scoreit.controller.api;

import com.scoreit.scoreit.dto.CreateUserRequest;
import com.scoreit.scoreit.dto.UserResponse;
import com.scoreit.scoreit.model.User;
import com.scoreit.scoreit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("")
    public List<UserResponse> getUsers(){
        return userService.getUsers();
    }
}
