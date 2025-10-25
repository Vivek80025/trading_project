package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.domain.WithdrawalStatus;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.Withdrawal;
import com.vivek.Treading_Backend.repository.WithdrawalRepository;
import com.vivek.Treading_Backend.service.WithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithDrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();

        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> optionalWithdrawal = withdrawalRepository.findById(withdrawalId);

        if(optionalWithdrawal.isEmpty()){
            throw new Exception("withdrawal not found...");
        }

        Withdrawal withdrawal = optionalWithdrawal.get();

        withdrawal.setDate(LocalDateTime.now());

        if(accept){
            withdrawal.setStatus(WithdrawalStatus.SUCCESS);
        }
        else {
            withdrawal.setStatus(WithdrawalStatus.DECLINE);
        }
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
