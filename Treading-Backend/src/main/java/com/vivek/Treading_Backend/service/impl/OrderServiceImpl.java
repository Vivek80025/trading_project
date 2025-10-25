package com.vivek.Treading_Backend.service.impl;

import com.vivek.Treading_Backend.domain.OrderStatus;
import com.vivek.Treading_Backend.domain.OrderType;
import com.vivek.Treading_Backend.model.*;
import com.vivek.Treading_Backend.repository.OrderItemRepository;
import com.vivek.Treading_Backend.repository.OrderRepository;
import com.vivek.Treading_Backend.service.AssetService;
import com.vivek.Treading_Backend.service.OrderService;
import com.vivek.Treading_Backend.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final WalletService walletService;
    private final AssetService assetService;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType type) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setOrderType(type);
        order.setOrderItem(orderItem);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimeStamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()) {
            throw new Exception("Order not found...");
        }
        return optionalOrder.get();
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    OrderItem createOrderItem(Coin coin,double quantity,double sellPrice,double buyPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setSellPrice(sellPrice);
        orderItem.setBuyPrice(buyPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin,double quantity,User user) throws Exception {
        if(quantity<=0){
            throw new Exception("Quantity should be greater than 0");
        }

        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = createOrderItem(coin,quantity,0,buyPrice);

        Order order = createOrder(user,orderItem,OrderType.BUY);
        orderItem.setOrder(order);

        orderItemRepository.save(orderItem);

        walletService.payOrderPayment(order,user);

        order.setOrderStatus(OrderStatus.SUCCESS);

        Order savedOrder = orderRepository.save(order);

        //we need to create asset
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
                user.getId(),
                coin.getId());

        if(oldAsset==null){
            oldAsset = assetService.createAsset(user,coin,quantity);
        }
        else{
            assetService.updateAsset(oldAsset.getId(),oldAsset.getQuantity() + quantity);
        }

        return savedOrder;
    }

    @Transactional
    public Order sellAsset(Coin coin,double quantity,User user) throws Exception {
        if(quantity<=0){
            throw new Exception("Quantity should be greater than 0");
        }

        double sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());

        if(assetToSell!=null){

            double buyPrice = assetToSell.getBuyPrice();
            OrderItem orderItem = createOrderItem(coin,quantity,sellPrice,buyPrice);

            Order order = createOrder(user,orderItem,OrderType.SELL);

            orderItem.setOrder(order);

            if(assetToSell.getQuantity()>=quantity){

                orderItemRepository.save(orderItem);

                order.setOrderStatus(OrderStatus.SUCCESS);

                Order savedOrder = orderRepository.save(order);

                walletService.payOrderPayment(order,user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if(updatedAsset.getQuantity() * coin.getCurrentPrice()<=1){
                    assetService.deleteAsset(updatedAsset.getId());
                }

                return savedOrder;

            }
            throw new Exception("Insufficient quantity to sell...");

        }
        throw new Exception("asset not found...");

    }

    @Transactional
    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {

        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin,quantity,user);
        }

        else if(orderType.equals(OrderType.SELL)){
            return sellAsset(coin,quantity,user);
        }
        throw new Exception("Invalid order type...");
    }
}
