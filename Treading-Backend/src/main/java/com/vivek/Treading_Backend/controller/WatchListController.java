package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.WatchList;
import com.vivek.Treading_Backend.service.CoinService;
import com.vivek.Treading_Backend.service.UserService;
import com.vivek.Treading_Backend.service.WatchListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/watchlist")
public class WatchListController {
    private final WatchListService watchListService;
    private final UserService userService;
    private final CoinService coinService;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        WatchList watchList = watchListService.findUserWatchList(user);

        return ResponseEntity.ok(watchList);
    }

//    @PostMapping("/create")
//    public ResponseEntity<WatchList> createWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
//        User user = userService.findUserProfileByJwt(jwt);
//        WatchList watchList = watchListService.createWatchList(user);
//
//        return ResponseEntity.ok(watchList);
//    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(@PathVariable Long watchListId) throws Exception {

        WatchList watchList = watchListService.getWatchListById(watchListId);

        return ResponseEntity.ok(watchList);
    }

    @PostMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable String coinId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Coin coin = coinService.findById(coinId);

        Coin addedCoin = watchListService.addItemToWatchList(coin,user);

        return ResponseEntity.ok(addedCoin);
    }
}
