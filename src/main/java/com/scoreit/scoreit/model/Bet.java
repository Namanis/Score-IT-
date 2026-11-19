package com.scoreit.scoreit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "bets", uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "match_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id")
    private GameParticipant participant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id")
    private Match match;

    private int predictedHomeScore;
    private int predictedAwayScore;
    private Integer pointsEarned;

    private Instant createdAt = Instant.now();
}
