package com.vivek.Treading_Backend.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.vivek.Treading_Backend.domain.PaymentMethod;
import com.vivek.Treading_Backend.model.PaymentOrder;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.response.PaymentResponse;
import com.vivek.Treading_Backend.service.PaymentService;
import com.vivek.Treading_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    private final UserService userService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder paymentOrder = paymentService.createPaymentOrder(user,amount,paymentMethod);

        PaymentResponse paymentResponse;

        if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
            paymentResponse = paymentService.createRazorpayPaymentLink(user,amount,paymentOrder.getId());
        }
        else {
            paymentResponse = paymentService.createStripePaymentLink(user,amount,paymentOrder.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
