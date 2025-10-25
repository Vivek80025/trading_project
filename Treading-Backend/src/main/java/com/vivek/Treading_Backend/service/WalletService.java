package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.Order;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.Wallet;
import com.vivek.Treading_Backend.model.WalletTransaction;

import java.util.List;

public interface WalletService {
    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet,Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender,Wallet receiverWallet,Long money) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
    List<WalletTransaction> getWalletTransactions(Wallet wallet);
}
