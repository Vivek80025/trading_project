package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.WatchList;

public interface WatchListService {
    WatchList findUserWatchList(User user) throws Exception;

    WatchList createWatchList(User user);

    WatchList getWatchListById(Long id) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;

}
