package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.domain.WalletTransactionType;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.Wallet;
import com.vivek.Treading_Backend.model.WalletTransaction;
import com.vivek.Treading_Backend.model.Withdrawal;
import com.vivek.Treading_Backend.repository.WalletTransactionRepository;
import com.vivek.Treading_Backend.service.UserService;
import com.vivek.Treading_Backend.service.WalletService;
import com.vivek.Treading_Backend.service.WithdrawalService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class WithdrawalController {
    private final WithdrawalService withdrawalService;
    private final UserService userService;
    private final WalletService walletService;
    private final WalletTransactionRepository walletTransactionRepository;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<Withdrawal> withdrawalRequest(@RequestHeader("Authorization") String jwt,
                                                        @PathVariable Long amount) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithDrawal(amount,user);

        walletService.addBalance(userWallet,-amount);

        //wallet transaction code
        WalletTransaction tx = new WalletTransaction();
        tx.setWallet(userWallet);
        tx.setType(WalletTransactionType.WITHDRAWAL);
        tx.setLocalDateTime(LocalDateTime.now());
        tx.setAmount(amount);
        tx.setPurpose("bank account Withdrawal");
        walletTransactionRepository.save(tx);


        return ResponseEntity.ok(withdrawal);

    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<Withdrawal> proceedWithdrawal(@RequestHeader("Authorization") String jwt,
                                                        @PathVariable Long id,
                                                        @PathVariable boolean accept) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal = withdrawalService.proceedWithdrawal(id,accept);

        Wallet userWallet = walletService.getUserWallet(user);

        if(!accept){
            walletService.addBalance(userWallet,withdrawal.getAmount());
        }

        return ResponseEntity.ok(withdrawal);

    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getUserWithdrawalHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawalList = withdrawalService.getUserWithdrawalHistory(user);
        return ResponseEntity.ok(withdrawalList);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawalList = withdrawalService.getAllWithdrawalRequest();
        return ResponseEntity.ok(withdrawalList);
    }


}