package com.scoreit.scoreit.config;

import com.scoreit.scoreit.model.Game;
import com.scoreit.scoreit.model.GameParticipant;
import com.scoreit.scoreit.model.User;
import com.scoreit.scoreit.repository.GameParticipantRepository;
import com.scoreit.scoreit.repository.GameRepository;
import com.scoreit.scoreit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameParticipantRepository participantRepository;

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
    }
}
