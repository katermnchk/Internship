package com.innowise.service;

import com.innowise.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderMetricsTest {

    private List<Order> orders;
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

    @BeforeEach
    void setUp() {
        customer1 = new Customer("C1", "Kate", "kate@mail.com",
                LocalDateTime.now().minusYears(2), 19, "Minsk");
        customer2 = new Customer("C2", "Arseniy", "senya@mail.com",
                LocalDateTime.now().minusYears(1), 18, "Bobruisk");
        customer3 = new Customer("C3", "Anna", "anna@mail.com",
                LocalDateTime.now().minusYears(3), 20, "Warsaw");

        orders = new ArrayList<>();

        OrderItem dress = new OrderItem("Dress", 1, 500.0, Category.CLOTHING);
        OrderItem book = new OrderItem("Book", 3, 20.0, Category.BOOKS);
        OrderItem laptop = new OrderItem("MacBook", 2, 30.0, Category.ELECTRONICS);

        orders.add(new Order("O1", LocalDateTime.now().minusDays(5), customer1,
                List.of(dress, book), OrderStatus.DELIVERED));
        orders.add(new Order("O2", LocalDateTime.now().minusDays(4), customer2,
                List.of(laptop, book), OrderStatus.DELIVERED));

        //order not delivered yet
        orders.add(new Order("O3", LocalDateTime.now().minusDays(3), customer1,
                List.of(book), OrderStatus.NEW));

        //cancelled order
        orders.add(new Order("O4", LocalDateTime.now().minusDays(2), customer3,
                List.of(new OrderItem("Laptop", 1, 1000.0, Category.ELECTRONICS)), OrderStatus.CANCELLED));

        //add more orders for frequent customer
        for (int i = 5; i <= 10; i++) {
            orders.add(new Order("O" + i, LocalDateTime.now(),
                    customer1, List.of(new OrderItem("Book", 1, 20.0, Category.BOOKS)), OrderStatus.DELIVERED));
        }
    }

    @Test
    void testGetUniqueCities() {
        Set<String> cities = OrderMetrics.getUniqueCities(orders);
        assertEquals(Set.of("Minsk", "Bobruisk", "Warsaw"), cities);
    }

    @Test
    void testGetTotalIncome() {
        double totalIncome = OrderMetrics.getTotalIncome(orders);
        //O1=500+3*20=560, O2=2*30+3*20=120, O5-O10=6*20=120, total 800
        assertEquals(800.0, totalIncome);
    }

    @Test
    void testGetMostPopularProduct() {
        String mostPopular = OrderMetrics.getMostPopularProduct(orders);
        //Book 3+3+6=12, Dress 1, MacBook 2, Laptop 1
        assertEquals("Book", mostPopular);
    }

    @Test
    void testGetAverageCheck() {
        double avgCheck = OrderMetrics.getAverageCheck(orders);
        //delivered orders: O1=560, O2=120, O5-O10=120 -> avg=800/8=100
        assertEquals(100.0, avgCheck);
    }

    @Test
    void testGetCustomers() {
        List<Customer> frequentCustomers = OrderMetrics.getCustomers(orders);
        //customer1 has 7 orders -> frequent
        assertEquals(1, frequentCustomers.size());
        assertEquals("Kate", frequentCustomers.get(0).getName());
    }

    @Test
    void testEmptyOrders() {
        List<Order> empty = new ArrayList<>();
        assertTrue(OrderMetrics.getUniqueCities(empty).isEmpty());
        assertEquals(0.0, OrderMetrics.getTotalIncome(empty));
        assertNull(OrderMetrics.getMostPopularProduct(empty));
        assertEquals(0.0, OrderMetrics.getAverageCheck(empty));
        assertTrue(OrderMetrics.getCustomers(empty).isEmpty());
    }

    @Test
    void testNoDeliveredOrders() {
        List<Order> newOrders = List.of(
                new Order("O1", LocalDateTime.now(), customer1,
                        List.of(new OrderItem("Book", 1, 20.0, Category.BOOKS)),
                        OrderStatus.NEW),
                new Order("O2", LocalDateTime.now(), customer2,
                        List.of(new OrderItem("Phone", 1, 500.0, Category.ELECTRONICS)),
                        OrderStatus.CANCELLED)
        );
        assertEquals(0.0, OrderMetrics.getTotalIncome(newOrders));
        assertEquals(0.0, OrderMetrics.getAverageCheck(newOrders));
    }

    @Test
    void testTieForMostPopularProduct() {
        List<Order> tiedOrders = List.of(
                new Order("O1", LocalDateTime.now(), customer1, List.of(
                        new OrderItem("Book", 2, 20.0, Category.BOOKS),
                        new OrderItem("T-Shirt", 2, 30.0, Category.CLOTHING)
                ), OrderStatus.DELIVERED)
        );
        String popular = OrderMetrics.getMostPopularProduct(tiedOrders);
        assertTrue(popular.equals("Book") || popular.equals("T-Shirt"));
    }
}
