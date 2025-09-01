package com.innowise;

public class CustomLinkedList<T> {
        private int size;
    private Node<T> head;
    private Node<T> tail;

    public CustomLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(T el) {
        Node<T> newNode = new Node<>(el);
        newNode.next = head;
        head = newNode;

        if (size == 0) {
            tail = newNode;
        }

        size++;
    }

    public void addLast(T el) {
        Node<T> newNode = new Node<>(el);
        if (size == 0) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

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
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        Node<T> newNode = new Node<>(el);
        newNode.next = current.next;
        current.next = newNode;

        size++;
    }


    public T getFirst() {
        if (head == null)
            throw new IllegalStateException("List is empty");
        return head.value;
    }

    public T getLast() {
        if (tail == null)
            throw new IllegalStateException("List is empty");
        return tail.value;
    }

    public T get(int index) {
        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.value;
    }

    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        T value = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return value;
    }

    public T removeLast() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        if (head == tail) {
            T value = head.value;
            head = tail = null;
            size = 0;
            return value;
        }

        Node<T> current = head;
        while (current.next != tail) {
            current = current.next;
        }

        T value = tail.value;
        tail = current;
        tail.next = null;
        size--;
        return value;
    }

    public T remove(int index) {
        checkIndex(index);

        if (index == 0) {
            return removeFirst();
        }

        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }

        T value = current.next.value;
        if (current.next == tail) {
            tail = current;
        }
        current.next = current.next.next;
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
        T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }
}