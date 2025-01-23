package com.big.dragons.service;

import com.big.dragons.client.GameApiClient;
import com.big.dragons.dto.Reputation;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvestigationServiceTest {

    private final GameApiClient gameApiClient = mock(GameApiClient.class);
    private final InvestigationService investigationService = new InvestigationService(gameApiClient);

    @Test
    void fetchInvestigationData_shouldReturnReputation_whenApiCallSucceeds() {
        String gameId = "test-game-id";
        Reputation mockReputation = new Reputation();
        mockReputation.setPeople(-4);
        mockReputation.setState(2);
        mockReputation.setUnderworld(0);

        when(gameApiClient.fetchInvestigationData(gameId)).thenReturn(mockReputation);

        Reputation result = investigationService.fetchInvestigationData(gameId);

        assertNotNull(result);
        assertEquals(-4, result.getPeople());
        assertEquals(2, result.getState());
        assertEquals(0, result.getUnderworld());
        verify(gameApiClient, times(1)).fetchInvestigationData(gameId);
    }

    @Test
    void fetchInvestigationData_shouldThrowRestClientException_whenApiCallFails() {
        String gameId = "test-game-id";
        when(gameApiClient.fetchInvestigationData(gameId)).thenThrow(new RestClientException("API error"));

        RestClientException exception = assertThrows(
                RestClientException.class,
                () -> investigationService.fetchInvestigationData(gameId)
        );

        assertEquals("API error", exception.getMessage());
        verify(gameApiClient, times(1)).fetchInvestigationData(gameId);
    }
}
