package com.big.dragons.runner;

import com.big.dragons.service.GameLogicService;
import com.big.dragons.service.TrainingModeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class StartupRunnerTest {

    @Mock
    private TrainingModeService trainingModeService;

    @Mock
    private GameLogicService gameLogicService;

    @InjectMocks
    private StartupRunner startupRunner;

    @Value("${training.games:0}")
    private int trainingGames;

    @BeforeEach
    void setUp() {
        doNothing().when(gameLogicService).playGame();
    }

    @Test
    void testRun_withTrainingModeEnabled() throws Exception {
        doNothing().when(trainingModeService).runTraining(anyInt());
        ReflectionTestUtils.setField(startupRunner, "trainingGames", 5);
        startupRunner.run();
        verify(trainingModeService, times(1)).runTraining(5);
        verify(gameLogicService, times(1)).playGame();
    }

    @Test
    void testRun_withTrainingModeDisabled() throws Exception {
        ReflectionTestUtils.setField(startupRunner, "trainingGames", 0);
        startupRunner.run();
        verify(trainingModeService, never()).runTraining(0);
        verify(gameLogicService, times(1)).playGame();
    }
}
