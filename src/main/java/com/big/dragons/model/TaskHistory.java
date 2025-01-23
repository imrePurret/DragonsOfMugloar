package com.big.dragons.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "task_history")
@Data
@Builder
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adId;
    private String message;
    private String probability;
    private int reward;
    private boolean success;

    private double reputationPeople;
    private double reputationState;
    private double reputationUnderworld;

    private int dragonLevel;
}
