package com.vivek.Treading_Backend.repository;

import com.vivek.Treading_Backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
