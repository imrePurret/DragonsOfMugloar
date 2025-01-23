package com.big.dragons.runner;

import com.big.dragons.service.GameLogicService;
import com.big.dragons.service.TrainingModeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);
    private final TrainingModeService trainingModeService;
    private final GameLogicService gameLogicService;

    @Value("${training.games:0}")
    private int trainingGames;

    @Override
    public void run(String... args) throws Exception {
        if (trainingGames > 0) {
            logger.info("Training mode enabled. Running {} training games.", trainingGames);
            trainingModeService.runTraining(trainingGames);
        } else {
            logger.info("Training mode disabled. Starting the main game.");
        }
        gameLogicService.playGame();
    }
}
