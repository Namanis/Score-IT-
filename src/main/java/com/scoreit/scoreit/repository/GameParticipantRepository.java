package com.scoreit.scoreit.repository;

import com.scoreit.scoreit.model.GameParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {
    List<GameParticipant> findByGame_IdOrderByScoreDesc(Long gameId);
    
    Optional<GameParticipant> findByUser_IdAndGame_Id(Long userId, Long gameId);
    
    boolean existsByUser_IdAndGame_Id(Long userId, Long gameId);
}
