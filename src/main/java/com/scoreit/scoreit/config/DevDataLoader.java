package com.scoreit.scoreit.config;

import com.scoreit.scoreit.model.*;
import com.scoreit.scoreit.repository.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameParticipantRepository participantRepository;
    private final MatchRepository matchRepository;
    private final BetRepository betRepository;

    @Override
    public void run(String @NonNull ... args) {
        User ana = new User();
        ana.setUsername("ana");
        ana.setEmail("ana@test.com");
        userRepository.save(ana);

        User ben = new User();
        ben.setUsername("ben");
        ben.setEmail("ben@test.com");
        userRepository.save(ben);

        Game game = new Game();
        game.setName("NBA Week 12");
        game.setCreatedBy(ana);
        gameRepository.save(game);

        GameParticipant anaJoins = new GameParticipant();
        anaJoins.setUser(ana);
        anaJoins.setGame(game);
        participantRepository.save(anaJoins);

        GameParticipant benJoins = new GameParticipant();
        benJoins.setUser(ben);
        benJoins.setGame(game);
        participantRepository.save(benJoins);

// A match that hasn't started: you can bet on it
        Match upcoming = new Match();
        upcoming.setGame(game);
        upcoming.setHomeTeam("Lakers");
        upcoming.setAwayTeam("Celtics");
        upcoming.setStartTime(Instant.now().plus(1, ChronoUnit.DAYS));
        matchRepository.save(upcoming);

// A match that already started, with bets: you can view them
        Match started = new Match();
        started.setGame(game);
        started.setHomeTeam("Warriors");
        started.setAwayTeam("Bulls");
        started.setStartTime(Instant.now().minus(1, ChronoUnit.HOURS));
        matchRepository.save(started);

        Bet anaBet = new Bet();
        anaBet.setParticipant(anaJoins);
        anaBet.setMatch(started);
        anaBet.setPredictedHomeScore(110);
        anaBet.setPredictedAwayScore(102);
        betRepository.save(anaBet);

        Bet benBet = new Bet();
        benBet.setParticipant(benJoins);
        benBet.setMatch(started);
        benBet.setPredictedHomeScore(98);
        benBet.setPredictedAwayScore(105);
        betRepository.save(benBet);
    }
}
