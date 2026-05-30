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

        System.out.println("=================================");
        System.out.println("NEW ORDER REQUEST");
        System.out.println("Item ID : " + request.getItemId());
        System.out.println("Quantity : " + request.getQuantity());
        System.out.println("Customer Type : " + request.getCustomerType());
        System.out.println("=================================");

        // Validate Quantity
        if (request.getQuantity() <= 0) {

            throw new IllegalArgumentException(
                    "Please enter a valid quantity greater than zero.");
        }

        // Find Inventory Item
        InventoryItem item = inventoryRepository
                .findById(request.getItemId())
                .orElseThrow(() -> {

                    System.out.println(
                            "ERROR : Inventory Item Not Found");

                    return new RuntimeException(
                            "Invalid Item ID. Please select a valid inventory item.");
                });

        System.out.println(
                "Inventory Item Found : "
                        + item.getName());

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

            System.out.println(
                    "Order Status : PROCESSED");

        } else {

            order.setStatus(OrderStatus.BACKORDERED);

            System.out.println(
                    "Order Status : BACKORDERED");
        }

        // Low Stock Warning
        if (item.getStockQuantity() < item.getRestockQuantity()) {

            System.out.println(
                    "WARNING : "
                            + item.getName()
                            + " stock is below threshold.");
        }

        inventoryRepository.save(item);

        Order savedOrder = orderRepository.save(order);

        System.out.println(
                "Order Saved Successfully");
        System.out.println(
                "Order ID : "
                        + savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }
}