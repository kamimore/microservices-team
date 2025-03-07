package com.microservice.order_services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.order_services.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    
}
