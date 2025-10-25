package com.vivek.Treading_Backend.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vivek.Treading_Backend.domain.PaymentMethod;
import com.vivek.Treading_Backend.domain.PaymentOrderStatus;
import com.vivek.Treading_Backend.model.PaymentOrder;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.repository.PaymentOrderRepository;
import com.vivek.Treading_Backend.response.PaymentResponse;
import com.vivek.Treading_Backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;


    @Override
    public PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("payment order not found..."));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        //paymentId receive from razorpay
        System.out.println("=================================================="+paymentOrder.getStatus());
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){

            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){

                RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                System.out.println("========================================================="+status);

                if(status.equals("captured")){

                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }

                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }

            //I will complete for stripe later...
        }

        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount, Long payment_order_id) {
        try {
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount * 100); // amount in paise
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("description", "Payment money");

            JSONObject customer = new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());

            paymentLinkRequest.put("customer",customer);

            JSONObject notify = new JSONObject();
            notify.put("email",true);

            paymentLinkRequest.put("notify", notify);
            paymentLinkRequest.put("reminder_enable", true);
//            paymentLinkRequest.put("callback_url", "url........ payment complete hone ke baad is url pe redirect hoga");
            paymentLinkRequest.put("callback_url", "https://webhook.site/3c262cd2-10b4-4f48-b229-aac84befc92a?payment_order_id="+payment_order_id);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = paymentLink.get("id");
            String paymentLinkUrl = paymentLink.get("short_url");

            PaymentResponse response = new PaymentResponse();
            response.setPayment_url(paymentLinkUrl);

            return response;

        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating Razorpay payment link", e);
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) {
        try {
            Stripe.apiKey = stripeSecretKey;

            // Build checkout session params
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setSuccessUrl("url...")
                    .setCancelUrl("url...")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount(amount * 100) // Stripe also expects smallest unit (paise)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order #" + orderId)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .setCustomerEmail(user.getEmail()) // Pre-fill customer email
                    .putMetadata("orderId", orderId.toString()) // attach metadata for tracking
                    .build();

            Session session = Session.create(params);

            // Build response
            PaymentResponse response = new PaymentResponse();
            response.setPayment_url(session.getUrl());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error creating Stripe payment link", e);
        }

    }
}
