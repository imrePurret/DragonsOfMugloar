package com.big.dragons.service;

import com.big.dragons.dto.Reputation;
import com.big.dragons.client.GameApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class InvestigationService {

    private final GameApiClient gameApiClient;

    public Reputation fetchInvestigationData(String gameId) throws RestClientException {
        return gameApiClient.fetchInvestigationData(gameId);
    }
}
