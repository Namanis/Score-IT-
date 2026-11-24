package com.scoreit.scoreit.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateMatchRequest(
        @NotBlank
        String homeTeam,
        @NotBlank
        String awayTeam,
        @NotNull
        @Future
        Instant startTime
) {
}
