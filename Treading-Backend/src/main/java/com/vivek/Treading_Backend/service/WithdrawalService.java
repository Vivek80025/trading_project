package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithDrawal(Long amount, User user);

    Withdrawal proceedWithdrawal(Long withdrawalId,boolean accept) throws Exception;

    List<Withdrawal> getUserWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();
}
