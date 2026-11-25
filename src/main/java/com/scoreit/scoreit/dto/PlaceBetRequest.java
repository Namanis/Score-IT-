package com.scoreit.scoreit.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlaceBetRequest(
        @NotNull
        Long userId,
        @NotNull
        @Min(0)
        Integer predictedHomeScore,
        @Min(0)
        @NotNull
        Integer predictedAwayScore
) {
}
