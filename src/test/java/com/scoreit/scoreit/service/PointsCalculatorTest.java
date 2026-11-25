package com.scoreit.scoreit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PointsCalculatorTest {
    private final PointsCalculator calculator = new PointsCalculator();

    @ParameterizedTest(name = "guess {0}-{1}, actual {2}-{3} -> {4} points")
    @CsvSource({
            // exact and close guesses, home win
            "100, 90,  100, 90,  50",
            "102, 92,  100, 90,  46",
            "101, 90,  100, 90,  42",
            "98,  90,  100, 90,  38",
            "99,  91,  100, 90,  38",
            "110, 85,  100, 90,  10",
            // away win
            "88,  105, 88,  105, 50",
            "90,  105, 88,  105, 38",
            // wrong winner or tie guess
            "95,  97,  100, 90,  0",
            "90,  100, 100, 90,  0",
            "100, 100, 100, 90,  0"
    })
    void calculatesPoints(int predictedHome, int predictedAway, int actualHome, int actualAway, int expected) {
        assertThat(calculator.calculate(predictedHome, predictedAway, actualHome, actualAway))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Exact score gives maximum points")
    void exactScoreGivesMaxPoints() {
        assertThat(calculator.calculate(115, 108, 115, 108))
                .isEqualTo(PointsCalculator.MAX_POINTS);
    }

    @Test
    @DisplayName("Team score points never go below zero")
    void teamScorePointsNeverNegative() {
        // Exact difference (+10) but both team scores miss by 30:
        // 5 winner + 25 difference + 0 + 0
        assertThat(calculator.calculate(130, 120, 100, 90)).isEqualTo(30);
    }

    @Test
    @DisplayName("Points always stay between 0 and MAX_POINTS")
    void pointsStayInRange() {
        for (int home = 60; home <= 140; home++) {
            for (int away = 60; away <= 140; away++) {
                int points = calculator.calculate(home, away, 100, 90);
                assertThat(points)
                        .as("guess %d-%d", home, away)
                        .isBetween(0, PointsCalculator.MAX_POINTS);
            }
        }
    }
}