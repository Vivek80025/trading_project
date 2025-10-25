package com.vivek.Treading_Backend.service;

import com.vivek.Treading_Backend.domain.OrderType;
import com.vivek.Treading_Backend.model.Coin;
import com.vivek.Treading_Backend.model.Order;
import com.vivek.Treading_Backend.model.OrderItem;
import com.vivek.Treading_Backend.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType type);

    Order getOrderById(Long orderid) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId,OrderType orderType,String assetSymbol);

    Order processOrder(Coin coin,double quantity,OrderType orderType,User user) throws Exception;

}
