package com.innowise;

public class CustomLinkedList<T> implements CustomList<T> {
    private int size;
    private Node<T> head;
    private Node<T> tail;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(T el) {
        Node<T> newNode = new Node<>(el);
        newNode.next = head;
        if (head != null) {
            head.prev = newNode;
        }
        head = newNode;

        if (size == 0) {
            tail = newNode;
        }

        size++;
    }

    @Override
    public void addLast(T el) {
        Node<T> newNode = new Node<>(el);
        newNode.prev = tail;
        if (size == 0) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    @Override
    public void add(int index, T el) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        if (index == 0) {
            addFirst(el);
            return;
        }
        if (index == size) {
            addLast(el);
            return;
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        Node<T> newNode = new Node<>(el);
        Node<T> prevNode = current.prev;

        newNode.next = current;
        newNode.prev = prevNode;
        prevNode.next = newNode;
        current.prev = newNode;

        size++;
    }

    @Override
    public T getFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }
        return head.value;
    }

    @Override
    public T getLast() {
        if (tail == null)
            throw new IllegalStateException("List is empty");
        return tail.value;
    }

    @Override
    public T get(int index) {
        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    @Override
    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        T value = head.value;
        head = head.next;

        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }

        size--;
        return value;
    }

    @Override
    public T removeLast() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        T value = tail.value;
        tail = tail.prev;

        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }

        size--;
        return value;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);

        if (index == 0) {
            return removeFirst();
        }
        if (index == size - 1) {
            return removeLast();
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        T value = current.value;
        Node<T> prevNode = current.prev;
        Node<T> nextNode = current.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        size--;
        return value;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    private static class Node<T> {
        final T value;
        Node<T> prev;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }
}