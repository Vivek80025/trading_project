package com.vivek.Treading_Backend.controller;

import com.vivek.Treading_Backend.domain.OrderType;
import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.Order;
import com.vivek.Treading_Backend.model.User;
import com.vivek.Treading_Backend.request.CreateOrderReq;
import com.vivek.Treading_Backend.service.CoinService;
import com.vivek.Treading_Backend.service.OrderService;
import com.vivek.Treading_Backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final CoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderReq req
            ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        Coin coin = coinService.findById(req.getCoinId());

        OrderType orderType = req.getOrderType();

        Order order = orderService.processOrder(coin, req.getQuantity(), orderType, user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        throw new Exception("You don't have access");
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUSer(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(required = false) String asset_symbol

    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Order> orders = orderService.getAllOrdersOfUser(user.getId(),orderType,asset_symbol);

        return ResponseEntity.ok(orders);

    }
}
