package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Dto.OrdersRequestDto;
import com.example.demo.Model.InventoryItem;
import com.example.demo.Model.Order;
import com.example.demo.Model.OrderStatus;
import com.example.demo.Repository.InventoryRepository;
import com.example.demo.Repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(
            OrderRepository orderRepository,
            InventoryRepository inventoryRepository) {

        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Order createOrder(OrdersRequestDto request) {

        // Validate Quantity
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        // Find Inventory Item
        InventoryItem item = inventoryRepository
                .findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException(
                        "Inventory Item with ID "
                                + request.getItemId()
                                + " not found"));

        Order order = new Order();

        order.setInventoryItem(item);
        order.setQuantity(request.getQuantity());
        order.setCustomerType(request.getCustomerType());

        // Check Stock Availability
        if (item.getStockQuantity() >= request.getQuantity()) {

            item.setStockQuantity(
                    item.getStockQuantity()
                            - request.getQuantity());

            order.setStatus(OrderStatus.PROCESSED);

        } else {

            order.setStatus(OrderStatus.BACKORDERED);
        }

        // Low Stock Warning
        if (item.getStockQuantity() < item.getRestockQuantity()) {

            System.out.println(
                    "WARNING: Item "
                            + item.getName()
                            + " stock is below threshold.");
        }

        inventoryRepository.save(item);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }
}