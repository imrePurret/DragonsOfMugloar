package com.big.dragons.service;

import com.big.dragons.dto.Item;
import com.big.dragons.dto.PurchaseResponse;
import com.big.dragons.client.GameApiClient;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);
    private final GameApiClient gameApiClient;

    public List<Item> fetchShopItems(String gameId) {
        try {
            return gameApiClient.getShopItems(gameId);
        } catch (Exception e) {
            logger.error("Failed to fetch shop items for game ID: {}. Error: {}", gameId, e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    public PurchaseResponse buyItem(String gameId, String itemId) {
        try {
            PurchaseResponse response = gameApiClient.buyItem(gameId, itemId);

            if (response.isShoppingSuccess()) {
                return response;
            }

            logger.warn("Item purchase failed: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("Error purchasing item: {}", e.getMessage(), e);
            return null;
        }
    }
}
