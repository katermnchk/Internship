package com.innowise;

public interface CustomList<T> {
    int size();
    void addFirst(T el);
    void addLast(T el);
    void add(int index, T el);
    T getFirst();
    T getLast();
    T get(int index);
    T removeFirst();
    T removeLast();
    T remove(int index);
}
