package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.Wallet;
import com.vivek.Treading_Backend.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {
    List<WalletTransaction> findByWallet(Wallet wallet);
}
