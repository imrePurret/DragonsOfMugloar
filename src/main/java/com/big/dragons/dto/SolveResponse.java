package com.big.dragons.dto;

import lombok.Data;

@Data
public class SolveResponse {
    private boolean success;
    private int score;
    private int gold;
    private int lives;
    private int turn;
    private String message;
}
