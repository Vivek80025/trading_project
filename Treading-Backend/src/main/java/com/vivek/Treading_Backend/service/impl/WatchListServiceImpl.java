package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.WatchList;
import com.vivek.Treading_Backend.repository.WatchListRepository;
import com.vivek.Treading_Backend.service.WatchListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepository watchListRepository;

    @Override
    public WatchList findUserWatchList(User user) throws Exception {
        WatchList watchList = watchListRepository.findByUserId(user.getId());
        if(watchList==null){
            watchList = createWatchList(user);
        }

        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList getWatchListById(Long id) throws Exception {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(id);

        return optionalWatchList.orElseThrow(() -> new Exception("WatchList not found..."));
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) throws Exception {
        WatchList watchList = findUserWatchList(user);

        if(watchList.getCoins().contains(coin)){
            watchList.getCoins().remove(coin);
        }
        else watchList.getCoins().add(coin);

        watchListRepository.save(watchList);

        return coin;
    }
}
