package com.innowise.service;

import com.innowise.model.Category;
import com.innowise.model.Customer;
import com.innowise.model.Order;
import com.innowise.model.OrderItem;
import com.innowise.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OrderMetricsTest {

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
    }

    @Test
    void givenOrders_WhenGetUniqueCities_ThenReturnAllCities() {
        var orders = List.of(
                new Order("O1", LocalDateTime.now(), customer1, List.of(), OrderStatus.NEW),
                new Order("O2", LocalDateTime.now(), customer2, List.of(), OrderStatus.NEW),
                new Order("O3", LocalDateTime.now(), customer3, List.of(), OrderStatus.NEW)
        );

        var cities = OrderMetrics.getUniqueCities(orders);

        assertAll(
                () -> assertEquals(3, cities.size()),
                () -> assertTrue(cities.contains("Minsk")),
                () -> assertTrue(cities.contains("Bobruisk")),
                () -> assertTrue(cities.contains("Warsaw"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrdersForIncome")
    void givenOrders_WhenGetTotalIncome_ThenReturnCorrectSum(List<Order> orders, double expectedIncome) {
        assertEquals(expectedIncome, OrderMetrics.getTotalIncome(orders));
    }

    @ParameterizedTest
    @MethodSource("provideOrdersForMostPopularProduct")
    void givenOrders_WhenGetMostPopularProduct_ThenReturnCorrectProduct(List<Order> orders, Set <String> expectedProducts) {

        if (expectedProducts.isEmpty()) {
            assertThrows(NoSuchElementException.class, () -> OrderMetrics.getMostPopularProduct(orders));
        } else {
            String popular = OrderMetrics.getMostPopularProduct(orders);
            assertAll(
                    () -> assertNotNull(popular),
                    () ->  assertTrue(expectedProducts.contains(popular))
            );
        }
    }

    @ParameterizedTest
    @MethodSource("provideOrdersForAverageCheck")
    void givenOrders_WhenGetAverageCheck_ThenReturnCorrectValue(List<Order> orders, double expectedAvg) {
        assertEquals(expectedAvg, OrderMetrics.getAverageCheck(orders));
    }

    @ParameterizedTest
    @CsvSource ({
            "6, true",
            "5, false",
            "7, true",
            "4, false"
    })
    void givenOrders_WhenGetCustomersWithMoreThanFiveOrders_ThenReturnCorrectCustomers(int orderCount, boolean expectedPresent) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < orderCount; i++) {
            orders.add(new Order("O" + (i + 1), LocalDateTime.now(), customer1,
                    List.of(new OrderItem("Book", 1, 20.0, Category.BOOKS)), OrderStatus.DELIVERED));
        }

        orders.add(new Order("O7", LocalDateTime.now(), customer2,
                List.of(new OrderItem("Phone", 1, 500.0, Category.ELECTRONICS)), OrderStatus.DELIVERED));


        var frequentCustomers = OrderMetrics.getCustomers(orders);

        assertEquals(expectedPresent, frequentCustomers.stream().anyMatch(c -> c.getName().equals("Kate")));
    }

    @Test
    void givenOrders_WhenGetOrdersEmpty_ThenReturnZero() {
        List<Order> empty = Collections.emptyList();

        assertAll (
                () -> assertTrue(OrderMetrics.getUniqueCities(empty).isEmpty()),
                () -> assertEquals(0.0, OrderMetrics.getTotalIncome(empty)),
                () -> assertThrows(NoSuchElementException.class, () -> OrderMetrics.getMostPopularProduct(empty)),
                () -> assertEquals(0.0, OrderMetrics.getAverageCheck(empty)),
                () -> assertTrue(OrderMetrics.getCustomers(empty).isEmpty())
        );
    }

    @Test
    void givenOrdersWithoutDelivered_WhenGetMetrics_ThenReturnZero() {
        var newOrders = List.of(
                new Order("O1", LocalDateTime.now(), customer1,
                        List.of(new OrderItem("Book", 1, 20.0, Category.BOOKS)),
                        OrderStatus.NEW),
                new Order("O2", LocalDateTime.now(), customer2,
                        List.of(new OrderItem("Phone", 1, 500.0, Category.ELECTRONICS)),
                        OrderStatus.CANCELLED)
        );

        assertAll(
                () -> assertEquals(0.0, OrderMetrics.getTotalIncome(newOrders)),
                () -> assertEquals(0.0, OrderMetrics.getAverageCheck(newOrders))
        );
    }

    @Test
    void givenOrdersWithTie_WhenGetMostPopularProduct_ThenReturnOneOfThem() {
        var tiedOrders = List.of(
                new Order("O1", LocalDateTime.now(), customer1, List.of(
                        new OrderItem("Book", 2, 20.0, Category.BOOKS),
                        new OrderItem("T-Shirt", 2, 30.0, Category.CLOTHING)
                ), OrderStatus.DELIVERED)
        );
        String popular = OrderMetrics.getMostPopularProduct(tiedOrders);

        assertAll (
                () -> assertNotNull(popular),
                () -> assertTrue(Set.of("Book", "T-Shirt").contains(popular))
        );
    }

    @Test
    void givenOrderWithoutItems_WhenGetMetrics_ThenReturnDefaults() {
        var orders = List.of(
                new Order("O1", LocalDateTime.now(), customer1, Collections.emptyList(), OrderStatus.DELIVERED)
        );

        assertAll (
                () -> assertEquals(0.0, OrderMetrics.getTotalIncome(orders)),
                () -> assertEquals(0.0, OrderMetrics.getAverageCheck(orders)),
                () -> assertThrows(NoSuchElementException.class, () -> OrderMetrics.getMostPopularProduct(orders))
        );
    }

    @Test
    void givenOrdersWithZeroQuantity_WhenGetMetrics_ThenIgnoreSuchItems() {
        var orders = List.of(
                new Order("O1", LocalDateTime.now(), customer1,
                        List.of(new OrderItem("Book", 0, 20.0, Category.BOOKS)),
                        OrderStatus.DELIVERED)
        );

        assertAll (
                () -> assertEquals(0.0, OrderMetrics.getTotalIncome(orders)),
                () -> assertEquals(0.0, OrderMetrics.getAverageCheck(orders)),
                () -> assertThrows(NoSuchElementException.class, () -> OrderMetrics.getMostPopularProduct(orders))
        );
    }


    private static Stream<Object[]> provideOrdersForIncome() {
        return Stream.of(
                new Object[]{Collections.emptyList(), 0.0},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com", LocalDateTime.now(), 19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS)), OrderStatus.DELIVERED)
                ), 40.0},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com", LocalDateTime.now(), 19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS)), OrderStatus.DELIVERED),
                        new Order("O2", LocalDateTime.now(),
                                new Customer("C2","Arseniy","senya@mail.com", LocalDateTime.now(),18,"Bobruisk"),
                                List.of(new OrderItem("Dress",1,50.0,Category.CLOTHING)), OrderStatus.DELIVERED)
                ), 90.0}
        );
    }

    private static Stream<Object[]> provideOrdersForMostPopularProduct() {
        return Stream.of(
                new Object[]{Collections.emptyList(), Collections.emptySet()},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com",LocalDateTime.now(),19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS)), OrderStatus.DELIVERED)
                ), Set.of("Book")},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com",LocalDateTime.now(),19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS),
                                        new OrderItem("T-Shirt",2,30.0,Category.CLOTHING)), OrderStatus.DELIVERED)
                ), Set.of("Book","T-Shirt")}
        );
    }

    private static Stream<Object[]> provideOrdersForAverageCheck() {
        return Stream.of(
                new Object[]{Collections.emptyList(), 0.0},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com", LocalDateTime.now(),19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS)), OrderStatus.DELIVERED)
                ), 40.0},
                new Object[]{List.of(
                        new Order("O1", LocalDateTime.now(),
                                new Customer("C1","Kate","kate@mail.com", LocalDateTime.now(),19,"Minsk"),
                                List.of(new OrderItem("Book",2,20.0,Category.BOOKS)), OrderStatus.DELIVERED),
                        new Order("O2", LocalDateTime.now(),
                                new Customer("C2","Arseniy","senya@mail.com", LocalDateTime.now(),18,"Bobruisk"),
                                List.of(new OrderItem("Dress",1,60.0,Category.CLOTHING)), OrderStatus.DELIVERED)
                ), 50.0} // (40+60)/2=50
        );
    }
}
