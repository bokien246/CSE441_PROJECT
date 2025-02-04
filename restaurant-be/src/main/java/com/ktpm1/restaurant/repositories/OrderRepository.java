package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.OrderStatus;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByOrderTimeBetween(Pageable pageable, Instant start, Instant end);
//    List<Order> findByOrderTimeBetweenOrEndTimeBetween(Instant start1, Instant end1, Instant start2, Instant end2);
    List<Order> findByOrderTimeBetween(Instant start, Instant end);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByUser(Pageable pageable, User user);
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
}
