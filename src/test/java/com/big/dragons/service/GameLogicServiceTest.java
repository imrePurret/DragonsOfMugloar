package com.big.dragons.service;

import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.dto.Item;
import com.big.dragons.dto.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameLogicServiceTest {

    @Mock
    private GameService gameService;

    @Mock
    private ShopService shopService;

    @Mock
    private MessageService messageService;

    @Mock
    private TaskHistoryService taskHistoryService;

    @InjectMocks
    private GameLogicService gameLogicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(gameLogicService, "gameService", gameService);
        ReflectionTestUtils.setField(gameLogicService, "shopService", shopService);
        ReflectionTestUtils.setField(gameLogicService, "messageService", messageService);
        ReflectionTestUtils.setField(gameLogicService, "taskHistoryService", taskHistoryService);
    }

    @Test
    void testPlayGame_withLivesRemaining() {
        when(gameService.getLives()).thenReturn(5).thenReturn(4).thenReturn(3).thenReturn(2).thenReturn(1).thenReturn(0);
        when(gameService.getGold()).thenReturn(0);
        when(gameService.getScore()).thenReturn(0);
        when(gameService.getGameId()).thenReturn("game-id");

        Task task1 = new Task();
        task1.setAdId("1");
        task1.setMessage("Task 1");
        task1.setProbability("Sure thing");
        task1.setReward(10);
        task1.setEncrypted(0);

        Task task2 = new Task();
        task2.setAdId("2");
        task2.setMessage("Task 2");
        task2.setProbability("No way");
        task2.setReward(10);
        task2.setEncrypted(0);

        when(messageService.fetchMessages(any())).thenReturn(List.of(task1, task2)).thenReturn(List.of());

        Item item = new Item();
        item.setId("hpot");
        item.setName("Health potion");
        when(shopService.fetchShopItems(any())).thenReturn(List.of(item));

        when(taskHistoryService.getSuccessRates()).thenReturn(Map.of("Sure thing", 0.9));

        SolveResponse solveResponse = new SolveResponse();
        solveResponse.setSuccess(true);
        solveResponse.setScore(20);
        when(messageService.solveMessage(any(), any())).thenReturn(solveResponse);

        gameLogicService.playGame();

        verify(gameService, times(1)).startNewGame();
        verify(messageService, times(2)).fetchMessages(any());
        verify(gameService, times(1)).updateGameState(solveResponse);

        assertEquals(0, gameService.getLives());
    }

    @Test
    void testSelectBestTask_withValidTasks() {
        Task task1 = new Task();
        task1.setAdId("1");
        task1.setMessage("task1");
        task1.setProbability("Sure thing");
        task1.setReward(10);
        task1.setEncrypted(0);

        Task task2 = new Task();
        task2.setAdId("2");
        task2.setMessage("task2");
        task2.setProbability("Impossible");
        task2.setReward(5);
        task2.setEncrypted(0);

        List<Task> tasks = List.of(task1, task2);

        Map<String, Double> successRates = Map.of("Sure thing", 0.8);

        Task result = gameLogicService.selectBestTask(tasks, successRates);

        assertNotNull(result);
        assertEquals("1", result.getAdId());
    }

    @Test
    void testSolveTask() {
        Task task = new Task();
        task.setAdId("1");
        task.setMessage("task1");
        task.setProbability("Sure thing");
        task.setReward(10);
        task.setEncrypted(0);

        SolveResponse solveResponse = new SolveResponse();
        solveResponse.setSuccess(true);

        when(messageService.solveMessage(any(), any())).thenReturn(solveResponse);

        gameLogicService.solveTask(task);

        verify(messageService, times(1)).solveMessage(any(), any());
        verify(gameService, times(1)).updateGameState(solveResponse);
    }

    @Test
    void testBuyItems_withSufficientGold() {
        Item item = new Item();
        item.setId("hpot");
        item.setName("Health Potion");
        item.setCost(50);

        when(gameService.getGold()).thenReturn(100).thenReturn(50).thenReturn(0);  // Return 100 initially, then 50 after purchase
        when(shopService.fetchShopItems(any())).thenReturn(List.of(item));

        gameLogicService.buyItems();

        verify(shopService, times(1)).fetchShopItems(any());
        verify(gameService, times(1)).updateGameState((PurchaseResponse) any());
    }

    @Test
    void testLogCurrentStatus() {
        when(gameService.getLives()).thenReturn(3);
        when(gameService.getScore()).thenReturn(100);
        when(gameService.getGold()).thenReturn(50);

        gameLogicService.logCurrentStatus();

        verify(gameService, times(1)).getLives();
        verify(gameService, times(1)).getScore();
        verify(gameService, times(1)).getGold();
    }
}
