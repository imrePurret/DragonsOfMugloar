package com.big.dragons.service;

import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.dto.Item;
import com.big.dragons.dto.Task;
import com.big.dragons.dto.Reputation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class TrainingModeService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingModeService.class);
    private final GameService gameService;
    private final MessageService messageService;
    private final InvestigationService investigationService;
    private final TaskHistoryService taskHistoryService;
    private final ShopService shopService;

    public void runTraining(int numberOfGames) {
        logger.info("Starting training mode for {} games.", numberOfGames);

        for (int i = 0; i < numberOfGames; i++) {
            logger.info("Starting training game {}", i + 1);

            String gameId = startNewGame();

            while (gameService.getLives() > 0) {
                if (!processGameRound(gameId)) {
                    break;
                }

                attemptToBuyRandomItem();
            }

            logger.info("Training game {} completed.", i + 1);
        }

        logger.info("Training mode completed for {} games.", numberOfGames);
    }

    private String startNewGame() {
        gameService.startNewGame();
        return gameService.getGameId();
    }

    boolean processGameRound(String gameId) {
        Reputation reputation = investigationService.fetchInvestigationData(gameId);
        List<Task> tasks = messageService.fetchMessages(gameService.getGameId());
        tasks = filterInvalidTasks(tasks);

        if (tasks.isEmpty()) {
            logger.warn("No valid tasks available. Ending game early.");
            return false;
        }

        Task selectedTask = selectRandomTask(tasks);
        SolveResponse solveResponse = messageService.solveMessage(gameId, selectedTask.getAdId());
        gameService.updateGameState(solveResponse);
        taskHistoryService.recordTask(selectedTask, solveResponse.isSuccess(), reputation, gameService.getLevel());

        logger.info("Task '{}' solved: {} | Current Lives: {}", selectedTask.getMessage(), solveResponse.isSuccess(), gameService.getLives());

        if (gameService.getLives() == 0) {
            logger.info("Game over!");
            return false;
        }

        return true;
    }

    Task selectRandomTask(List<Task> tasks) {
        int randomIndex = ThreadLocalRandom.current().nextInt(tasks.size());
        return tasks.get(randomIndex);
    }

    private List<Task> filterInvalidTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getEncrypted() <= 0)
                .toList();
    }

    void attemptToBuyRandomItem() {
        List<Item> items = shopService.fetchShopItems(gameService.getGameId());

        if (items.isEmpty()) {
            logger.warn("No items available in the shop.");
            return;
        }

        int maxCost = items.stream()
                .mapToInt(Item::getCost)
                .max()
                .orElse(0);

        if (gameService.getGold() <= maxCost) {
            logger.debug("Insufficient gold to buy the most expensive item. Available gold: {}, Max item cost: {}", gameService.getGold(), maxCost);
            return;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(items.size());
        Item randomItem = items.get(randomIndex);

        PurchaseResponse purchaseResponse = shopService.buyItem(gameService.getGameId(), randomItem.getId());
        gameService.updateGameState(purchaseResponse);

        if (purchaseResponse.isShoppingSuccess()) {
            logger.debug("Successfully purchased random item: {}", randomItem.getName());
        } else {
            logger.warn("Failed to purchase random item: {}", randomItem.getName());
        }
    }
}
