package com.big.dragons.service;

import com.big.dragons.dto.Item;
import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.client.GameApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    private GameApiClient gameApiClient;

    @InjectMocks
    private ShopService shopService;

    @Test
    void testFetchShopItems_ShouldReturnItems_WhenItemsAreFetchedSuccessfully() {
        String gameId = "game123";
        List<Item> items = List.of(new Item(), new Item());
        when(gameApiClient.getShopItems(gameId)).thenReturn(items);

        List<Item> result = shopService.fetchShopItems(gameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(gameApiClient, times(1)).getShopItems(gameId);
    }

    @Test
    void testFetchShopItems_ShouldReturnEmptyList_WhenNoItemsAreFetched() {
        String gameId = "game123";
        when(gameApiClient.getShopItems(gameId)).thenReturn(List.of());

        List<Item> result = shopService.fetchShopItems(gameId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gameApiClient, times(1)).getShopItems(gameId);
    }

    @Test
    void testFetchShopItems_ShouldReturnEmptyList_WhenAnExceptionOccurs() {
        String gameId = "game123";
        when(gameApiClient.getShopItems(gameId)).thenThrow(new RuntimeException("API error"));

        List<Item> result = shopService.fetchShopItems(gameId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(gameApiClient, times(1)).getShopItems(gameId);
    }

    @Test
    void testBuyItem_ShouldReturnPurchaseResponse_WhenPurchaseIsSuccessful() {
        String gameId = "game123";
        String itemId = "item123";
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setShoppingSuccess(true);
        when(gameApiClient.buyItem(gameId, itemId)).thenReturn(purchaseResponse);

        PurchaseResponse result = shopService.buyItem(gameId, itemId);

        assertNotNull(result);
        assertTrue(result.isShoppingSuccess());
        verify(gameApiClient, times(1)).buyItem(gameId, itemId);
    }

    @Test
    void testBuyItem_ShouldReturnNull_WhenPurchaseFails() {
        String gameId = "game123";
        String itemId = "item123";
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setShoppingSuccess(false);
        when(gameApiClient.buyItem(gameId, itemId)).thenReturn(purchaseResponse);

        PurchaseResponse result = shopService.buyItem(gameId, itemId);

        assertNull(result);
        verify(gameApiClient, times(1)).buyItem(gameId, itemId);
    }

    @Test
    void testBuyItem_ShouldReturnNull_WhenAnExceptionOccurs() {
        String gameId = "game123";
        String itemId = "item123";
        when(gameApiClient.buyItem(gameId, itemId)).thenThrow(new RuntimeException("API error"));

        PurchaseResponse result = shopService.buyItem(gameId, itemId);

        assertNull(result);
        verify(gameApiClient, times(1)).buyItem(gameId, itemId);
    }
}
