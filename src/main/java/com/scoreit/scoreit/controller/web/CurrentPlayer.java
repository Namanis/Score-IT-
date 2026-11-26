package com.scoreit.scoreit.controller.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;

@Component
@SessionScope
@Getter
@Setter
public class CurrentPlayer implements Serializable {

    private Long userId;

    public Long requireUserId() {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create a player first");
        }
        return userId;
    }
}
