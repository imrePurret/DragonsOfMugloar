package com.big.dragons.dto;

import lombok.Data;

@Data
public class PurchaseResponse {
    private boolean shoppingSuccess;
    private int gold;
    private int lives;
    private int level;
    private int turn;
}
