package com.big.dragons.service;

import com.big.dragons.dto.Task;
import com.big.dragons.dto.Reputation;
import com.big.dragons.model.TaskHistory;
import com.big.dragons.repository.TaskHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;

    public Map<String, Double> getSuccessRates() {
        List<Object[]> results = taskHistoryRepository.calculateSuccessRates();
        Map<String, Double> successRates = new LinkedHashMap<>();

        for (Object[] result : results) {
            String probability = (String) result[0];
            Double avgSuccessRate = (Double) result[1];
            successRates.put(probability, avgSuccessRate);
        }

        return successRates;
    }

    public void recordTask(Task task, boolean success, Reputation reputation, int dragonLevel) {
        TaskHistory taskHistory = TaskHistory.builder()
                .adId(task.getAdId())
                .message(task.getMessage())
                .probability(task.getProbability())
                .reward(task.getReward())
                .success(success)
                .reputationPeople(reputation.getPeople())
                .reputationState(reputation.getState())
                .reputationUnderworld(reputation.getUnderworld())
                .dragonLevel(dragonLevel)
                .build();

        taskHistoryRepository.save(taskHistory);
    }
}
