package com.big.dragons.service;

import com.big.dragons.dto.Task;
import com.big.dragons.dto.Reputation;
import com.big.dragons.model.TaskHistory;
import com.big.dragons.repository.TaskHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskHistoryServiceTest {

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @InjectMocks
    private TaskHistoryService taskHistoryService;

    private Task task;
    private Reputation reputation;

    @BeforeEach
    public void setup() {
        task = new Task();
        task.setProbability("Sure thing");
        task.setMessage("Test message");
        task.setAdId("ad123");
        task.setReward(35);

        reputation = new Reputation();
        reputation.setPeople(-4);
        reputation.setState(2);
        reputation.setUnderworld(0);
    }

    @Test
    void testGetSuccessRates_ShouldReturnSuccessRates_WhenResultsAreFetchedSuccessfully() {
        Object[] result1 = {"Walk in the park", 0.85};
        Object[] result2 = {"Hmmm...", 0.65};
        List<Object[]> results = List.of(result1, result2);
        when(taskHistoryRepository.calculateSuccessRates()).thenReturn(results);

        Map<String, Double> successRates = taskHistoryService.getSuccessRates();

        assertNotNull(successRates);
        assertEquals(2, successRates.size());
        assertEquals(0.85, successRates.get("Walk in the park"));
        assertEquals(0.65, successRates.get("Hmmm..."));
        verify(taskHistoryRepository, times(1)).calculateSuccessRates();
    }

    @Test
    void testGetSuccessRates_ShouldReturnEmptyMap_WhenNoResultsAreFetched() {
        List<Object[]> results = List.of();
        when(taskHistoryRepository.calculateSuccessRates()).thenReturn(results);

        Map<String, Double> successRates = taskHistoryService.getSuccessRates();

        assertNotNull(successRates);
        assertTrue(successRates.isEmpty());
        verify(taskHistoryRepository, times(1)).calculateSuccessRates();
    }

    @Test
    void testRecordTask_ShouldSaveTaskHistory_WhenValidDataIsProvided() {
        when(taskHistoryRepository.save(any(TaskHistory.class))).thenReturn(null);

        taskHistoryService.recordTask(task, true, reputation, 5);

        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));
    }

    @Test
    void testRecordTask_ShouldSaveTaskHistory_WhenSuccessIsFalse() {
        when(taskHistoryRepository.save(any(TaskHistory.class))).thenReturn(null);

        taskHistoryService.recordTask(task, false, reputation, 5);

        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));
    }
}
