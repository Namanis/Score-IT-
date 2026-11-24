package com.scoreit.scoreit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGameRequest(
        @NotBlank
        String name,

        @NotNull
        Long userId
) { }
