package com.microservice.order_services.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microservice.order_services.dto.OrderLineItemsDto;
import com.microservice.order_services.dto.OrderRequest;
import com.microservice.order_services.model.Order;
import com.microservice.order_services.model.OrderLineItems;
import com.microservice.order_services.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
        .map(this::mapToDto).toList();

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);

    }

    public OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;
    }
}
