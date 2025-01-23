package com.big.dragons.dto;

import lombok.Data;

@Data
public class Task {
    private String adId;
    private String message;
    private int reward;
    private int expiresIn;
    private int encrypted;
    private String probability;
}
