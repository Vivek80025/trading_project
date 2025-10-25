package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.model.*;
import com.vivek.Treading_Backend.service.OrderService;
import com.vivek.Treading_Backend.service.PaymentService;
import com.vivek.Treading_Backend.service.UserService;
import com.vivek.Treading_Backend.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@AllArgsConstructor
public class walletController {
    private final WalletService walletService;

    private final UserService userService;

    private final OrderService orderService;

    private final PaymentService paymentService;


    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/transfer/{walletId}/{amount}")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @PathVariable Long amount
            ) throws Exception {
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser,receiverWallet,amount);

        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order,user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "payment_order_id") Long paymentOrderId,
            @RequestParam(name = "payment_id") String paymentId

    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);

        boolean status = paymentService.proceedPaymentOrder(paymentOrder,paymentId);

        if(status){
            wallet = walletService.addBalance(wallet,paymentOrder.getAmount());
        }
        return ResponseEntity.ok(wallet);
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<WalletTransaction>> getWalletTransactions(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        List<WalletTransaction> transactions = walletService.getWalletTransactions(wallet);

        return ResponseEntity.ok(transactions);
    }

}
