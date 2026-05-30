package com.example.demo.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.OrdersRequestDto;
import com.example.demo.Model.InventoryItem;
import com.example.demo.Model.Order;
import com.example.demo.Repository.InventoryRepository;
import com.example.demo.Service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;
    private final InventoryRepository inventoryRepository;

    public OrderController(
            OrderService orderService,
            InventoryRepository inventoryRepository) {

        this.orderService = orderService;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping
    public Order createOrder(
            @RequestBody OrdersRequestDto request) {

        return orderService.createOrder(request);
    }

    @GetMapping
    public List<Order> getAllOrders() {

        return orderService.getAllOrders();
    }

    @GetMapping("/inventory")
    public List<InventoryItem> getInventory() {

        return inventoryRepository.findAll();
    }
}