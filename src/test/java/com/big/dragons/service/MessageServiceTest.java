package com.big.dragons.service;

import com.big.dragons.dto.Task;
import com.big.dragons.dto.SolveResponse;
import com.big.dragons.client.GameApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private GameApiClient gameApiClient;

    @InjectMocks
    private MessageService messageService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    void testFetchMessages_ShouldReturnTasks_WhenMessagesAreFetchedSuccessfully() {
        String gameId = "game123";
        List<Task> tasks = List.of(new Task(), new Task());
        when(gameApiClient.getMessages(gameId)).thenReturn(tasks);

        List<Task> result = messageService.fetchMessages(gameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(gameApiClient, times(1)).getMessages(gameId);
    }

    @Test
    void testFetchMessages_ShouldReturnEmptyList_WhenNoMessagesAreFetched() {
        String gameId = "game123";
        when(gameApiClient.getMessages(gameId)).thenReturn(List.of());

        List<Task> result = messageService.fetchMessages(gameId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gameApiClient, times(1)).getMessages(gameId);
    }

    @Test
    void testFetchMessages_ShouldReturnEmptyList_WhenAnExceptionOccurs() {
        String gameId = "game123";
        when(gameApiClient.getMessages(gameId)).thenThrow(new RuntimeException("API error"));

        List<Task> result = messageService.fetchMessages(gameId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gameApiClient, times(1)).getMessages(gameId);
    }

    @Test
    void testSolveMessage_ShouldReturnSolveResponse_WhenSuccessful() {
        String gameId = "game123";
        String adId = "ad123";
        SolveResponse solveResponse = new SolveResponse();
        solveResponse.setSuccess(true);
        when(gameApiClient.solveTask(gameId, adId)).thenReturn(solveResponse);

        SolveResponse result = messageService.solveMessage(gameId, adId);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        verify(gameApiClient, times(1)).solveTask(gameId, adId);
    }

    @Test
    void testSolveMessage_ShouldReturnNull_WhenResponseIsNull() {
        String gameId = "game123";
        String adId = "ad123";
        when(gameApiClient.solveTask(gameId, adId)).thenReturn(null);

        SolveResponse result = messageService.solveMessage(gameId, adId);

        assertNull(result);
        verify(gameApiClient, times(1)).solveTask(gameId, adId);
    }

    @Test
    void testSolveMessage_ShouldReturnNull_WhenAnExceptionOccurs() {
        String gameId = "game123";
        String adId = "ad123";
        when(gameApiClient.solveTask(gameId, adId)).thenThrow(new RuntimeException("API error"));

        SolveResponse result = messageService.solveMessage(gameId, adId);

        assertNull(result);
        verify(gameApiClient, times(1)).solveTask(gameId, adId);
    }
}
