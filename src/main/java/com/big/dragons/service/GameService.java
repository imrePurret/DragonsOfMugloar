package com.big.dragons.service;

import com.big.dragons.dto.Game;
import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.client.GameApiClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final GameApiClient gameApiClient;
    private Game game;

    public void startNewGame() {
        try {
            game = gameApiClient.startNewGame();
            logger.info("Game started with ID: {}", game.getGameId());
        } catch (Exception e) {
            logger.error("Error starting game: {}", e.getMessage());
        }
    }

    public int getLives() {
        validateGameInitialized();
        return game.getLives();
    }

    public int getGold() {
        validateGameInitialized();
        return game.getGold();
    }

    public int getScore() {
        validateGameInitialized();
        return game.getScore();
    }

    public int getLevel() {
        validateGameInitialized();
        return game.getLevel();
    }

    public String getGameId() {
        validateGameInitialized();
        return game.getGameId();
    }

    public void updateGameState(PurchaseResponse response) {
        validateGameInitialized();
        updateGameState(game.getScore(), response.getGold(), response.getLives(), response.getTurn(), response.getLevel());
    }

    public void updateGameState(SolveResponse response) {
        validateGameInitialized();
        updateGameState(response.getScore(), response.getGold(), response.getLives(), response.getTurn(), game.getLevel());
    }

    private void updateGameState(int score, int gold, int lives, int turn, int level) {
        validateGameInitialized();
        game.setScore(score);
        game.setGold(gold);
        game.setLives(lives);
        game.setTurn(turn);
        game.setLevel(level);
        logger.debug("Game updated: {}", game);
    }

    private void validateGameInitialized() {
        if (game == null) {
            throw new IllegalStateException("Game has not been initialized. Call startNewGame() first.");
        }
    }
}
