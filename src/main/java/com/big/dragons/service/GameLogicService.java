package com.big.dragons.service;

import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.dto.Item;
import com.big.dragons.dto.Task;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameLogicService {

    private static final Logger logger = LoggerFactory.getLogger(GameLogicService.class);
    private final GameService gameService;
    private final ShopService shopService;
    private final MessageService messageService;
    private final TaskHistoryService taskHistoryService;

    public void playGame() {
        gameService.startNewGame();

        Map<String, Double> successRates = taskHistoryService.getSuccessRates();

        while (gameService.getLives() > 0) {
            List<Task> tasks = messageService.fetchMessages(gameService.getGameId());
            Task taskToSolve = selectBestTask(tasks, successRates);

            if (gameService.getLives() == 0 || taskToSolve == null) {
                logger.info("Game over!");
                break;
            }
            solveTask(taskToSolve);

            while (gameService.getGold() >= 50 && gameService.getLives() < 7 || gameService.getGold() > 100) {
                buyItems();
            }

            logCurrentStatus();
        }
        logger.info("Total score: {}", gameService.getScore());
    }

    Task selectBestTask(List<Task> tasks, Map<String, Double> successRates) {
        List<Task> validTasks = filterInvalidTasks(tasks);
        List<Task> filteredTasks = filterDiamondTasks(validTasks);

        if (filteredTasks.isEmpty()) {
            logger.warn("No suitable tasks available.");
            return selectFallbackTask(validTasks, tasks);
        }

        return filteredTasks.stream()
                .max(Comparator.comparingDouble((Task task) -> successRates.getOrDefault(task.getProbability(), 0.0))
                        .thenComparingDouble(Task::getReward))
                .orElse(null);
    }

    private Task selectFallbackTask(List<Task> validTasks, List<Task> tasks) {
        if (!validTasks.isEmpty()) {
            return validTasks.getFirst();
        } else if (!tasks.isEmpty()) {
            return tasks.getFirst();
        }
        return null;
    }

    private List<Task> filterInvalidTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getEncrypted() <= 0)
                .toList();
    }

    // Data analysis - outlier
    private List<Task> filterDiamondTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> !task.getMessage().contains("Steal super awesome diamond"))
                .toList();
    }

    void solveTask(Task task) {
        SolveResponse solveResponse = messageService.solveMessage(gameService.getGameId(), task.getAdId());
        logger.info("Solved task: {}, success: {}, score: {}, gold: {}, lives: {}",
                task.getAdId(),
                solveResponse.isSuccess(),
                solveResponse.getScore(),
                solveResponse.getGold(),
                solveResponse.getLives());
        gameService.updateGameState(solveResponse);
    }

    void buyItems() {
        List<Item> items = shopService.fetchShopItems(gameService.getGameId());

        if (items == null || items.isEmpty()) {
            logger.info("No items available in the shop.");
            return;
        }

        // Prioritize health potion when lives are low
        if (gameService.getLives() < 7) {
            items.stream()
                    .filter(item -> item.getId().equalsIgnoreCase("hpot") && canBuyItem(item))
                    .findFirst()
                    .ifPresent(this::buyItem);
        } else items.stream()
                .filter(item -> item.getId().equalsIgnoreCase("cs") && canBuyItem(item))
                .findAny()
                .ifPresent(this::buyItem);
    }

    private boolean canBuyItem(Item item) {
        return item.getCost() <= gameService.getGold();
    }

    private void buyItem(Item item) {
        PurchaseResponse purchaseResponse = shopService.buyItem(gameService.getGameId(), item.getId());
        gameService.updateGameState(purchaseResponse);
        logger.info("Purchased item: {}", item.getName());
    }

    void logCurrentStatus() {
        logger.info("Game Status: Lives = {}, Score = {}, Gold = {}", gameService.getLives(), gameService.getScore(), gameService.getGold());
    }
}
