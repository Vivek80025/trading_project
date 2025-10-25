package com.vivek.Treading_Backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.service.CoinService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
@AllArgsConstructor
public class CoinController {
    private final CoinService coinService;
    private final ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coin> coins = coinService.getCoinList(page);
        return ResponseEntity.ok(coins);
    }

    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(
            @PathVariable String coinId,
            @RequestParam("days") int days) throws Exception {

        String response = coinService.getMarketChart(coinId,days);

        JsonNode jsonNode = objectMapper.readTree(response);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {

        String response = coinService.getTop50CoinsByMarketCapRank();

        JsonNode jsonNode = objectMapper.readTree(response);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam("keyword") String keyword) throws Exception {

        String response = coinService.searchCoin(keyword);

        JsonNode jsonNode = objectMapper.readTree(response);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/trending")
    ResponseEntity<JsonNode> getTrendingCoins() throws Exception {

        String response = coinService.getTrendingCoins();

        JsonNode jsonNode = objectMapper.readTree(response);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {

        String response = coinService.getCoinDetails(coinId);

        JsonNode jsonNode = objectMapper.readTree(response);
        return ResponseEntity.ok(jsonNode);
    }


}
