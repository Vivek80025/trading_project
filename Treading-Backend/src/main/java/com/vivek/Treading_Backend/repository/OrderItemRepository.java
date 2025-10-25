package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
