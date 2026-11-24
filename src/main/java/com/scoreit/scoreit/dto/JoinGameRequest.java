package com.scoreit.scoreit.dto;

import jakarta.validation.constraints.NotNull;

public record JoinGameRequest(
        @NotNull
        Long userId
) {
}
