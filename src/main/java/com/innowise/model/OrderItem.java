package com.innowise.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}
