package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin,String> {
}
