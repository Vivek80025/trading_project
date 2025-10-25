package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByUserId(Long userId);
}
