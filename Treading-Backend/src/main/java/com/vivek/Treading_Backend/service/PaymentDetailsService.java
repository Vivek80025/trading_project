package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.model.PaymentDetails;
import com.vivek.Treading_Backend.model.User;

public interface PaymentDetailsService {
    PaymentDetails addPaymentDetails(String accountNumber,
                                     String accountHolderName,
                                     String ifsc,
                                     String bankName,
                                     User user);


    PaymentDetails getUserPaymentDetails(User user);
}
