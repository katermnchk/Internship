package com.innowise.service;


import com.innowise.model.Customer;
import com.innowise.model.Order;
import com.innowise.model.OrderStatus;
import com.innowise.model.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class OrderMetrics {

    public static List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(Order::getCustomer)
                .map(Customer::getCity)
                .distinct()
                .collect(Collectors.toList());
    }

    public static double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getItems)
                .flatMap(List::stream)
                .filter(i -> i.getQuantity() > 0)
                .mapToDouble(i -> i.getQuantity() * i.getPrice())
                .sum();
    }

    public static String getMostPopularProduct(List<Order> orders) {
        return orders.stream()
                .map(Order::getItems)
                .flatMap(List::stream)
                .filter(i -> i.getQuantity() > 0)
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NoSuchElementException("There is no product with positive quantity"));
    }

    public static double getAverageCheck(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(o -> o.getItems().stream()
                        .filter(i -> i.getQuantity() > 0)
                        .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum())
                .average()
                .orElse(0.0);
    }

    public static List<Customer> getCustomers(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }
}
