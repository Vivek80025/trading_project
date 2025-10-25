package com.vivek.Treading_Backend.model;

import com.vivek.Treading_Backend.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;


    private WalletTransactionType type;

    private LocalDateTime localDateTime;

    //only come when wallet-to-wallet transfer
    private String transferId;

    private String purpose;

    private Long amount;
}
