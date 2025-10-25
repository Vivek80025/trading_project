package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.domain.OrderType;
import com.vivek.Treading_Backend.domain.WalletTransactionType;
import com.vivek.Treading_Backend.model.Order;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.model.Wallet;
import com.vivek.Treading_Backend.model.WalletTransaction;
import com.vivek.Treading_Backend.repository.WalletRepository;
import com.vivek.Treading_Backend.repository.WalletTransactionRepository;
import com.vivek.Treading_Backend.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet==null){
            wallet = new Wallet();
            wallet.setUser(user);
            wallet = walletRepository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

        wallet.setBalance(newBalance);

        wallet  = walletRepository.save(wallet);

        //Save transaction
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setType(WalletTransactionType.ADD_MONEY);
        transaction.setLocalDateTime(LocalDateTime.now());
        transaction.setAmount(money);
        transaction.setPurpose("Wallet top-up");
        walletTransactionRepository.save(transaction);

        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        if(optionalWallet.isEmpty()){
            throw new Exception("wallet not found...");
        }
        return optionalWallet.get();
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long money) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(money))<0){
            throw new Exception("Insufficient balance...");
        }

        BigDecimal senderBalance = senderWallet.getBalance()
                                   .subtract(BigDecimal.valueOf(money));

        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);



        BigDecimal receiverBalance = receiverWallet.getBalance()
                                     .add(BigDecimal.valueOf(money));

        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);

        //Record sender transaction
        WalletTransaction senderTx = new WalletTransaction();
        senderTx.setWallet(senderWallet);
        senderTx.setType(WalletTransactionType.WALLET_TRANSFER);
        senderTx.setLocalDateTime(LocalDateTime.now());
        senderTx.setAmount(money);
        senderTx.setPurpose("Transfer to wallet ID " + receiverWallet.getId());
        walletTransactionRepository.save(senderTx);

        // ✅ Record receiver transaction
        WalletTransaction receiverTx = new WalletTransaction();
        receiverTx.setWallet(receiverWallet);
        receiverTx.setType(WalletTransactionType.ADD_MONEY);
        receiverTx.setLocalDateTime(LocalDateTime.now());
        receiverTx.setAmount(money);
        receiverTx.setPurpose("Received from wallet ID " + senderWallet.getId());
        walletTransactionRepository.save(receiverTx);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if(order.getOrderType().equals(OrderType.BUY)){
            if(wallet.getBalance().compareTo(order.getPrice())<0){
                throw new Exception("insufficient balance for this transaction...");
            }
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            wallet.setBalance(newBalance);

            wallet = walletRepository.save(wallet);

            //BUY transaction
            WalletTransaction tx = new WalletTransaction();
            tx.setWallet(wallet);
            tx.setType(WalletTransactionType.BUY_ASSET);
            tx.setLocalDateTime(LocalDateTime.now());
            tx.setAmount(order.getPrice().longValue());
            tx.setPurpose("Payment for Order ID " + order.getId());
            walletTransactionRepository.save(tx);

        }
        if(order.getOrderType().equals(OrderType.SELL)){
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);

            wallet = walletRepository.save(wallet);

            //SELL transaction
            WalletTransaction tx = new WalletTransaction();
            tx.setWallet(wallet);
            tx.setType(WalletTransactionType.SELL_ASSET);
            tx.setLocalDateTime(LocalDateTime.now());
            tx.setAmount(order.getPrice().longValue());
            tx.setPurpose("Earnings from Order ID " + order.getId());
            walletTransactionRepository.save(tx);
        }

        return wallet;
    }

    //Get all transactions for a wallet
    public List<WalletTransaction> getWalletTransactions(Wallet wallet) {
        return walletTransactionRepository.findByWallet(wallet);
    }
}
