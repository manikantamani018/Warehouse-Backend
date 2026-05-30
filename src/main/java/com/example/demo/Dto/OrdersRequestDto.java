package com.example.demo.Dto;

import com.example.demo.Model.CustomerType;

import lombok.Data;

@Data
public class OrdersRequestDto {

    private Long itemId;
    private int quantity;
    private CustomerType customerType;
}