package com.big.dragons.service;

import com.big.dragons.client.GameApiClient;
import com.big.dragons.dto.Game;
import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameApiClient gameApiClient;

    @InjectMocks
    private GameService gameService;

    private Game mockGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockGame = new Game();
        mockGame.setGameId("test-game-id");
        mockGame.setLives(3);
        mockGame.setGold(100);
        mockGame.setScore(50);
        mockGame.setLevel(1);
    }

    @Test
    void startNewGame_initializesGameSuccessfully() {
        when(gameApiClient.startNewGame()).thenReturn(mockGame);

        gameService.startNewGame();

        assertEquals("test-game-id", gameService.getGameId());
        assertEquals(3, gameService.getLives());
        assertEquals(100, gameService.getGold());
        assertEquals(50, gameService.getScore());
        assertEquals(1, gameService.getLevel());

        verify(gameApiClient, times(1)).startNewGame();
    }

    @Test
    void startNewGame_handlesExceptionGracefully() {
        when(gameApiClient.startNewGame()).thenThrow(new RuntimeException("API error"));

        assertDoesNotThrow(() -> gameService.startNewGame());

        Exception exception = assertThrows(IllegalStateException.class, gameService::getGameId);
        assertEquals("Game has not been initialized. Call startNewGame() first.", exception.getMessage());

        verify(gameApiClient, times(1)).startNewGame();
    }

    @Test
    void updateGameState_withPurchaseResponse_updatesGame() {
        when(gameApiClient.startNewGame()).thenReturn(mockGame);

        gameService.startNewGame();

        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setGold(80);
        purchaseResponse.setLives(2);
        purchaseResponse.setTurn(5);
        purchaseResponse.setLevel(2);

        gameService.updateGameState(purchaseResponse);

        assertEquals(80, gameService.getGold());
        assertEquals(2, gameService.getLives());
    }

    @Test
    void updateGameState_withSolveResponse_updatesGame() {
        when(gameApiClient.startNewGame()).thenReturn(mockGame);

        gameService.startNewGame();

        SolveResponse solveResponse = new SolveResponse();
        solveResponse.setGold(70);
        solveResponse.setLives(4);
        solveResponse.setScore(120);
        solveResponse.setTurn(6);

        gameService.updateGameState(solveResponse);

        assertEquals(70, gameService.getGold());
        assertEquals(4, gameService.getLives());
        assertEquals(120, gameService.getScore());
    }

    @Test
    void getters_throwExceptionWhenGameNotInitialized() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, gameService::getGameId);
        assertEquals("Game has not been initialized. Call startNewGame() first.", exception.getMessage());
    }
}
