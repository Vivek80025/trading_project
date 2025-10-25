package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.model.PaymentDetails;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.PaymentDetailsRepository;
import com.vivek.Treading_Backend.service.PaymentDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user) {

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);

        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
