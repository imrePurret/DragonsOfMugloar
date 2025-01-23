package com.big.dragons.repository;

import com.big.dragons.model.TaskHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskHistoryRepositoryTest {

    @Autowired
    private TaskHistoryRepository taskHistoryRepository;

    @BeforeEach
    void setUp() {
        taskHistoryRepository.deleteAll();

        taskHistoryRepository.save(TaskHistory.builder()
                .adId("ad1")
                .message("Test message 1")
                .probability("Sure thing")
                .reward(100)
                .success(true)
                .reputationPeople(0.8)
                .reputationState(0.6)
                .reputationUnderworld(0.5)
                .dragonLevel(1)
                .build());

        taskHistoryRepository.save(TaskHistory.builder()
                .adId("ad2")
                .message("Test message 2")
                .probability("Probable")
                .reward(200)
                .success(false)
                .reputationPeople(0.7)
                .reputationState(0.5)
                .reputationUnderworld(0.4)
                .dragonLevel(2)
                .build());

        taskHistoryRepository.save(TaskHistory.builder()
                .adId("ad3")
                .message("Test message 3")
                .probability("Piece of cake")
                .reward(50)
                .success(true)
                .reputationPeople(0.6)
                .reputationState(0.4)
                .reputationUnderworld(0.3)
                .dragonLevel(3)
                .build());

        taskHistoryRepository.save(TaskHistory.builder()
                .adId("ad4")
                .message("Test message 4")
                .probability("Sure thing")
                .reward(150)
                .success(false)
                .reputationPeople(0.9)
                .reputationState(0.7)
                .reputationUnderworld(0.6)
                .dragonLevel(4)
                .build());
    }

    @Test
    void testCalculateSuccessRates() {
        List<Object[]> successRates = taskHistoryRepository.calculateSuccessRates();

        assertThat(successRates).isNotNull().isNotEmpty();

        assertThat(successRates.stream().anyMatch(result -> result[0].equals("Sure thing") && result[1].equals(0.5)))
                .isTrue();
        assertThat(successRates.stream().anyMatch(result -> result[0].equals("Probable") && result[1].equals(0.0)))
                .isTrue();
        assertThat(successRates.stream().anyMatch(result -> result[0].equals("Piece of cake") && result[1].equals(1.0)))
                .isTrue();
    }
}
