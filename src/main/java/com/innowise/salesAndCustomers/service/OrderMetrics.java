package com.innowise.salesAndCustomers.service;


import com.innowise.salesAndCustomers.model.Customer;
import com.innowise.salesAndCustomers.model.Order;
import com.innowise.salesAndCustomers.model.OrderStatus;
import com.innowise.salesAndCustomers.model.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderMetrics {
    //List of unique cities where orders came from
    public static Set<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(o -> o.getCustomer().getCity())
                .collect(Collectors.toSet());
    }

    //Total income for all completed orders
    public static double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(i -> i.getQuantity() * i.getPrice())
                .sum();
    }

    public static String getMostPopularProduct(List<Order> orders) {
        return orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    //Average check for successfully delivered orders
    public static double getAverageCheck(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(o -> o.getItems().stream()
                        .mapToDouble(i -> i.getPrice() * i.getQuantity()).sum())
                .average()
                .orElse(0.0);
    }

    //Customers who have more than 5 orders
    public static List<Customer> getCustomers(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .toList();
    }
}
