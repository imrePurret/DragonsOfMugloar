package com.big.dragons.service;

import com.big.dragons.dto.Task;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.client.GameApiClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final GameApiClient gameApiClient;

    public List<Task> fetchMessages(String gameId) {
        try {
            List<Task> tasks = gameApiClient.getMessages(gameId);

            if (tasks == null || tasks.isEmpty()) {
                return List.of();
            }

            return tasks;
        } catch (Exception e) {
            logger.error("Error fetching messages: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public SolveResponse solveMessage(String gameId, String adId) {
        try {
            SolveResponse response = gameApiClient.solveTask(gameId, adId);

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            logger.error("Error solving message: {}", e.getMessage(), e);
        }
        return null;
    }

}
