package com.vivek.Treading_Backend.request;

import com.vivek.Treading_Backend.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderReq {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
