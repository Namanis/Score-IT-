package com.scoreit.scoreit.service;

import org.springframework.stereotype.Component;

@Component
public class PointsCalculator {

    public static final int MAX_POINTS = 50;
    private static final int[] DIFFERENCE_POINTS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    private enum Outcome {HOME_WIN, AWAY_WIN, TIE}

    public int calculate(int predictedHome, int predictedAway, int actualHome, int actualAway) {
        int points = 0;
        boolean correctWinner = outcome(predictedHome, predictedAway) == outcome(actualHome, actualAway);
        int predictedDiff = difference(predictedHome, predictedAway);
        int actualDiff = difference(actualHome, actualAway);

        if (!correctWinner) {
            return points;
        } else {
            points += 5;
        }

        int miss = Math.abs(predictedDiff - actualDiff);
        int differenceBonus = miss < DIFFERENCE_POINTS.length ? DIFFERENCE_POINTS[miss] : 0;

        points += differenceBonus + teamScorePoints(predictedHome, actualHome) + teamScorePoints(predictedAway, actualAway);


        return points;
    }

    private Outcome outcome(int homeScore, int awayScore) {
        if (homeScore > awayScore) return Outcome.HOME_WIN;
        if (homeScore < awayScore) return Outcome.AWAY_WIN;
        return Outcome.TIE;
    }

    private int difference(int homeScore, int awayScore) {
        return Math.abs(homeScore - awayScore);
    }

    private int teamScorePoints(int predicted, int actual) {
        int miss = Math.abs(predicted - actual);
        return Math.max(0, 10 - miss);
    }
}
