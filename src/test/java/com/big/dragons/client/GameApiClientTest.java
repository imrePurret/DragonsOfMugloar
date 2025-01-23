package com.big.dragons.client;

import com.big.dragons.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameApiClientTest {

    private static final String BASE_URL = "https://dragonsofmugloar.com/api/v2";
    private static final String GAME_ID = "testGameId";
    private static final String ITEM_ID = "testItemId";
    private static final String AD_ID = "testAdId";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameApiClient gameApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startNewGame_shouldReturnGame() {
        Game mockGame = new Game();
        when(restTemplate.postForObject(BASE_URL + "/game/start", null, Game.class)).thenReturn(mockGame);

        Game result = gameApiClient.startNewGame();

        assertNotNull(result);
        verify(restTemplate, times(1)).postForObject(BASE_URL + "/game/start", null, Game.class);
    }

    @Test
    void getMessages_shouldReturnListOfTasks() {
        Task task1 = new Task();
        task1.setAdId("1");
        task1.setMessage("Task 1");
        task1.setProbability("Sure thing");
        task1.setReward(10);
        task1.setEncrypted(0);

        Task task2 = new Task();
        task2.setAdId("2");
        task2.setMessage("Task 2");
        task2.setProbability("No way");
        task2.setReward(5);
        task2.setEncrypted(0);

        List<Task> mockTasks = List.of(task1, task2);
        ResponseEntity<List<Task>> responseEntity = ResponseEntity.ok(mockTasks);
        when(restTemplate.exchange(
                eq(BASE_URL + "/" + GAME_ID + "/messages"),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<Task> result = gameApiClient.getMessages(GAME_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate, times(1)).exchange(
                eq(BASE_URL + "/" + GAME_ID + "/messages"),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void getShopItems_shouldReturnListOfItems() {
        Item item1 = new Item();
        item1.setId("item1");
        item1.setName("Item 1");
        item1.setCost(10);

        Item item2 = new Item();
        item2.setId("item2");
        item2.setName("Item 2");
        item2.setCost(15);

        List<Item> mockItems = List.of(item1, item2);
        ResponseEntity<List<Item>> responseEntity = ResponseEntity.ok(mockItems);
        when(restTemplate.exchange(
                eq(BASE_URL + "/" + GAME_ID + "/shop"),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<Item> result = gameApiClient.getShopItems(GAME_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate, times(1)).exchange(
                eq(BASE_URL + "/" + GAME_ID + "/shop"),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void buyItem_shouldReturnPurchaseResponse() {
        PurchaseResponse mockResponse = new PurchaseResponse();
        when(restTemplate.postForObject(BASE_URL + "/" + GAME_ID + "/shop/buy/" + ITEM_ID, null, PurchaseResponse.class))
                .thenReturn(mockResponse);

        PurchaseResponse result = gameApiClient.buyItem(GAME_ID, ITEM_ID);

        assertNotNull(result);
        verify(restTemplate, times(1))
                .postForObject(BASE_URL + "/" + GAME_ID + "/shop/buy/" + ITEM_ID, null, PurchaseResponse.class);
    }

    @Test
    void solveTask_shouldReturnSolveResponse() {
        SolveResponse mockResponse = new SolveResponse();
        when(restTemplate.postForObject(BASE_URL + "/" + GAME_ID + "/solve/" + AD_ID, null, SolveResponse.class))
                .thenReturn(mockResponse);

        SolveResponse result = gameApiClient.solveTask(GAME_ID, AD_ID);

        assertNotNull(result);
        verify(restTemplate, times(1))
                .postForObject(BASE_URL + "/" + GAME_ID + "/solve/" + AD_ID, null, SolveResponse.class);
    }

    @Test
    void fetchInvestigationData_shouldReturnReputation() {
        Reputation mockReputation = new Reputation();
        String url = BASE_URL + "/" + GAME_ID + "/investigate/reputation";
        when(restTemplate.postForObject(url, null, Reputation.class)).thenReturn(mockReputation);

        Reputation result = gameApiClient.fetchInvestigationData(GAME_ID);

        assertNotNull(result);
        verify(restTemplate, times(1)).postForObject(url, null, Reputation.class);
    }
}
