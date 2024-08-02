package com.javatechie.service;

import com.javatechie.common.dto.OrderRequestDTO;
import com.javatechie.common.mapper.OrderDTOtoEntityMapper;
import com.javatechie.common.mapper.OrderEntityToOutboxEntityMapper;
import com.javatechie.entity.Order;
import com.javatechie.entity.Outbox;
import com.javatechie.repository.OrderRepository;
import com.javatechie.repository.OutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderDTOtoEntityMapper orderDTOtoEntityMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OrderEntityToOutboxEntityMapper orderEntityToOutboxEntityMapper;


    @Transactional
    public Order createOrder(OrderRequestDTO orderRequestDTO) {

        Order order = orderDTOtoEntityMapper.map(orderRequestDTO);
        order = orderRepository.save(order);

        Outbox outbox = orderEntityToOutboxEntityMapper.map(order);
        outboxRepository.save(outbox);

        return order;
    }
}
