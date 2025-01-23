package com.big.dragons.service;

import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.dto.Item;
import com.big.dragons.dto.Task;
import com.big.dragons.dto.Reputation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingModeServiceTest {

    @Mock
    private GameService gameService;

    @Mock
    private MessageService messageService;

    @Mock
    private InvestigationService investigationService;

    @Mock
    private TaskHistoryService taskHistoryService;

    @Mock
    private ShopService shopService;

    @InjectMocks
    private TrainingModeService trainingModeService;

    private Task task;
    private Reputation reputation;
    private Item item;

    @BeforeEach
    void setup() {
        task = new Task();
        task.setAdId("ad123");
        task.setMessage("Test message");
        task.setProbability("Sure thing");
        task.setReward(75);

        reputation = new Reputation();
        reputation.setPeople(-6);
        reputation.setState(1);
        reputation.setUnderworld(0);

        item = new Item();
        item.setId("cl");
        item.setName("Claw");
        item.setCost(200);
    }

    @Test
    void testRunTraining_ShouldCompleteTraining_WhenValidGameData() {
        SolveResponse solveResponse = new SolveResponse();
        solveResponse.setSuccess(false);
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setShoppingSuccess(true);
        when(gameService.getLives()).thenReturn(3).thenReturn(2).thenReturn(1).thenReturn(0);
        when(gameService.getGold()).thenReturn(500);
        when(gameService.getLevel()).thenReturn(1);
        when(gameService.getGameId()).thenReturn("game123");
        when(investigationService.fetchInvestigationData(anyString())).thenReturn(reputation);
        when(messageService.fetchMessages(anyString())).thenReturn(List.of(task));
        when(messageService.solveMessage(anyString(), anyString())).thenReturn(solveResponse);
        when(shopService.fetchShopItems(anyString())).thenReturn(List.of(item));
        when(shopService.buyItem(anyString(), anyString())).thenReturn(purchaseResponse);

        trainingModeService.runTraining(1);

        verify(gameService, times(1)).startNewGame();
        verify(taskHistoryService, times(1)).recordTask(any(), eq(false), eq(reputation), eq(1));
        verify(shopService, times(1)).fetchShopItems(anyString());
        verify(shopService, times(1)).buyItem(anyString(), anyString());
    }

    @Test
    void testRunTraining_ShouldEndEarly_WhenNoValidTasks() {
        when(gameService.getLives()).thenReturn(3);
        when(gameService.getGameId()).thenReturn("game123");
        when(investigationService.fetchInvestigationData(anyString())).thenReturn(reputation);
        when(messageService.fetchMessages(anyString())).thenReturn(List.of());

        trainingModeService.runTraining(1);

        verify(gameService, times(1)).startNewGame();
        verify(messageService, times(1)).fetchMessages(anyString());
        verify(taskHistoryService, times(0)).recordTask(any(), anyBoolean(), any(), anyInt());
    }

    @Test
    void testAttemptToBuyRandomItem_ShouldPurchaseItem_WhenSufficientGold() {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setShoppingSuccess(true);
        when(gameService.getGold()).thenReturn(500);
        when(gameService.getGameId()).thenReturn("game123");
        when(shopService.fetchShopItems(anyString())).thenReturn(List.of(item));
        when(shopService.buyItem(anyString(), anyString())).thenReturn(purchaseResponse);

        trainingModeService.attemptToBuyRandomItem();

        verify(shopService, times(1)).buyItem(anyString(), anyString());
    }

    @Test
    void testAttemptToBuyRandomItem_ShouldNotPurchaseItem_WhenInsufficientGold() {
        when(gameService.getGold()).thenReturn(100);
        when(gameService.getGameId()).thenReturn("game123");
        when(shopService.fetchShopItems(anyString())).thenReturn(List.of(item));

        trainingModeService.attemptToBuyRandomItem();

        verify(shopService, times(0)).buyItem(anyString(), anyString());
    }

    @Test
    void testProcessGameRound_ShouldEndWhenNoTasks() {
        when(gameService.getGameId()).thenReturn("game123");
        when(investigationService.fetchInvestigationData(anyString())).thenReturn(reputation);
        when(messageService.fetchMessages(anyString())).thenReturn(List.of());

        boolean result = trainingModeService.processGameRound("game123");

        assertFalse(result);
    }

    @Test
    void testSelectRandomTask_ShouldReturnRandomTask_WhenTasksAvailable() {
        List<Task> tasks = List.of(new Task(), new Task());
        tasks.getFirst().setAdId("ad1");
        tasks.getFirst().setMessage("Task 1");
        tasks.getFirst().setProbability("Sure thing");
        tasks.getFirst().setReward(50);

        tasks.getLast().setAdId("ad2");
        tasks.getLast().setMessage("Task 2");
        tasks.getLast().setProbability("Impossible");
        tasks.getLast().setReward(30);

        Task selectedTask = trainingModeService.selectRandomTask(tasks);

        assertTrue(tasks.contains(selectedTask));
    }
}
