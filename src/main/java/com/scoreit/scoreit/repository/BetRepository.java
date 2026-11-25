package com.scoreit.scoreit.repository;

import com.scoreit.scoreit.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByMatch_Id(Long matchId);

    List<Bet> findByParticipant_Id(Long participantId);

    boolean existsByParticipant_IdAndMatch_Id(Long participantId, Long matchId);

    Optional<Bet> findByMatch_IdAndParticipant_User_Id(Long matchId, Long userId);
}

