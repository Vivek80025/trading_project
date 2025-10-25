package com.vivek.Treading_Backend.service;

import com.razorpay.RazorpayException;
import com.vivek.Treading_Backend.domain.PaymentMethod;
import com.vivek.Treading_Backend.model.PaymentOrder;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.response.PaymentResponse;

public interface PaymentService {

    PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount, Long payment_order_id);

    PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId);
}
