package com.innowise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomLinkedListTest {
    @Test
    void testAddFirst() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addFirst(1);
        list.addFirst(15);

        assertEquals(2, list.size());
        assertEquals(15, list.getFirst());
        assertEquals(1, list.getLast());
    }

    @Test
    void testAddLast() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(15);

        assertEquals(2, list.size());
        assertEquals(1, list.getFirst());
        assertEquals(15, list.getLast());
    }

    @Test
    void testAddByIndex() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(3);
        list.add(1, 2);

        assertEquals(3, list.size());
        assertEquals(1, list.getFirst());
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void testAddByIndexAtBounds() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(0, 10);
        assertEquals(1, list.size());
        assertEquals(10, list.getFirst());

        list.add(1, 20);
        assertEquals(2, list.size());
        assertEquals(20, list.getLast());
    }

    @Test
    void testAddByIndexThrows() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 5));
    }

    @Test
    void testRemoveFirst() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertEquals(1, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(2, list.getFirst());
    }

    @Test
    void testRemoveLast() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        assertEquals(3, list.removeLast());
        assertEquals(2, list.size());
        assertEquals(2, list.getLast());
    }

    @Test
    void testRemoveByIndex() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        int removed = list.remove(1);
        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(1, list.getFirst());
        assertEquals(3, list.getLast());
    }

    @Test
    void testRemoveElement() {
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.addLast("1");

        assertEquals("1", list.removeFirst());
        assertEquals(0, list.size());
        assertThrows(IllegalStateException.class, list::removeFirst);
    }

    @Test
    void testExceptionsOnEmptyList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertThrows(IllegalStateException.class, list::getFirst);
        assertThrows(IllegalStateException.class, list::getLast);
        assertThrows(IllegalStateException.class, list::removeFirst);
        assertThrows(IllegalStateException.class, list::removeLast);
    }

    @Test
    void testGetByIndexThrows() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));

        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }


}
