package com.innowise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {

    @ParameterizedTest
    @CsvSource({
            "addFirst, 1, 15, 15, 1",
            "addLast, 2, 25, 2, 25"
    })
    void givenEmptyList_WhenAddElements_ThenSizeAndElementsAreCorrect
            (String method, int value1, int value2, int expectedFirst, int expectedLast) {
        CustomList<Integer> list = new CustomLinkedList<>();
        if (method.equals("addFirst")) {
            list.addFirst(value1);
            list.addFirst(value2);
        } else {
            list.addLast(value1);
            list.addLast(value2);
        }

        assertAll(
                () -> assertEquals(2, list.size()),
                () -> assertEquals(expectedFirst, list.getFirst()),
                () -> assertEquals(expectedLast, list.getLast())
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 10, 10, 10",
            "1, 20, 10, 20"
    })
    void givenListWithOneElement_WhenAddByIndex_ThenElementInsertedCorrectly
            (int index, int value, int expectedFirst, int expectedLast) {
        CustomList<Integer> list = new CustomLinkedList<>();
        list.addLast(10);
        list.add(index, value);

        assertAll(
                () -> assertEquals(2, list.size(), "Size should be 2 after adding two elements"),
                () -> assertEquals(expectedFirst, list.getFirst(), "First element should match expected"),
                () -> assertEquals(expectedLast, list.getLast(), "Last element should match expected")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1})
    void givenEmptyList_WhenAddByInvalidIndex_ThenThrowIndexOutOfBounds(int index) {
        CustomList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(index, 5));
    }

    @Test
    void givenListWithElements_WhenRemoveFirst_ThenElementRemovedAndSizeUpdated(){
        CustomList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertAll(
                () -> assertEquals(1, list.removeFirst(), "Removed element should be 1"),
                () -> assertEquals(2, list.size(), "Size should be 2 after removal"),
                () -> assertEquals(2, list.getFirst(), "New first element should be 2")
        );
    }

    @Test
    void givenListWithElements_WhenRemoveLast_ThenElementRemovedAndSizeUpdated() {
        CustomList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertAll(
                () -> assertEquals(3, list.removeLast(), "Removed element should be 3"),
                () -> assertEquals(2, list.size(), "Size should be 2 after removal"),
                () -> assertEquals(2, list.getLast(), "New last element should be 2")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void givenListWithElements_WhenRemoveByIndex_ThenElementRemovedAndSizeUpdated(int index){
        CustomList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        int expectedSize = 2;
        int expectedRemoved = index + 1;
        int expectedFirst = index == 0 ? 2 : 1;
        int expectedLast = index == 2 ? 2 : 3;

        int removed = list.remove(index);
       assertAll(
               () -> assertEquals(expectedRemoved, removed, "Removed element should match expected"),
               () -> assertEquals(expectedSize, list.size(), "Size should be 2 after removal"),
               () -> assertEquals(expectedFirst, list.getFirst(), "First element should match expected"),
               () -> assertEquals(expectedLast, list.getLast(), "Last element should match expected")
       );
    }

    @Test
    void givenSingleElementList_WhenRemove_ThenEmpty() {
        CustomList<String> list = new CustomLinkedList<>();
        list.addLast("1");

        assertAll(
                () -> assertEquals("1", list.removeFirst(), "Removed element should be '1'"),
                () -> assertEquals(0, list.size(), "Size should be 0 after removal"),
                () -> assertThrows(IllegalStateException.class, list::getFirst, "getFirst should throw on empty list"),
                () -> assertThrows(IllegalStateException.class, list::getLast, "getLast should throw on empty list")
        );
    }

    @Test
    void givenEmptyList_WhenAccessMethodsCalled_ThenThrowIllegalStateException() {
        CustomList<Integer> list = new CustomLinkedList<>();

        assertAll(
                () -> assertThrows(IllegalStateException.class, list::getFirst, "getFirst should throw on empty list"),
                () -> assertThrows(IllegalStateException.class, list::getLast, "getLast should throw on empty list"),
                () -> assertThrows(IllegalStateException.class, list::removeFirst, "removeFirst should throw on empty list"),
                () -> assertThrows(IllegalStateException.class, list::removeLast, "removeLast should throw on empty list")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void givenEmptyOrSingleElementList_WhenGetByInvalidIndex_ThenThrowIndexOutOfBoundsException(int index) {
        CustomList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));

        list.addLast(1);
        if (index != 0) {
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
        }
    }

    @Test
    void givenListWithElements_WhenToStringCalled_ThenCorrectStringRepresentation() {
        CustomList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertEquals("[1, 2, 3]", list.toString(), "String representation should match");
    }
}
