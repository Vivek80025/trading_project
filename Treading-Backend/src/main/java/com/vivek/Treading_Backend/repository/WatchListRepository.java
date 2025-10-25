package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList,Long> {
    WatchList findByUserId(Long userId);
}
