package com.big.dragons.client;

import com.big.dragons.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameApiClient {

    private static final String BASE_URL = "https://dragonsofmugloar.com/api/v2";
    private final RestTemplate restTemplate;

    public Game startNewGame() throws RestClientException {
        String url = BASE_URL + "/game/start";
        return restTemplate.postForObject(url, null, Game.class);
    }

    public List<Task> getMessages(String gameId) throws RestClientException {
        String url = BASE_URL + "/" + gameId + "/messages";
        ResponseEntity<List<Task>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    public List<Item> getShopItems(String gameId) throws RestClientException {
        String url = BASE_URL + "/" + gameId + "/shop";
        ResponseEntity<List<Item>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    public PurchaseResponse buyItem(String gameId, String itemId) throws RestClientException {
        String url = BASE_URL + "/" + gameId + "/shop/buy/" + itemId;
        return restTemplate.postForObject(url, null, PurchaseResponse.class);
    }

    public SolveResponse solveTask(String gameId, String adId) throws RestClientException {
        String url = BASE_URL + "/" + gameId + "/solve/" + adId;
        return restTemplate.postForObject(url, null, SolveResponse.class);
    }

    public Reputation fetchInvestigationData(String gameId) throws RestClientException {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/{gameId}/investigate/reputation")
                .buildAndExpand(gameId)
                .toUriString();
        return restTemplate.postForObject(url, null, Reputation.class);
    }
}
