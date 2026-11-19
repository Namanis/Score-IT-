package com.scoreit.scoreit.repository;

import com.scoreit.scoreit.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByMatch_Id(Long matchId);

    List<Bet> findByParticipant_Id(Long participantId);

    boolean existsByParticipant_IdAndMatch_Id(Long participantId, Long matchId);
}

