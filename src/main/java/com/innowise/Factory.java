package com.innowise;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class Factory implements Runnable {
    private final BlockingQueue<Part> storage;
    private final Phaser phaser;
    private final int maxParts;
    private static final List<Part> AVAILABLE_PARTS = List.of(Part.HEAD, Part.TORSO, Part.HAND, Part.FEET);

    @Override
    public void run() {
        try {
            int todayCount = ThreadLocalRandom.current().nextInt(1, maxParts + 1);

            for (int i = 0; i < todayCount; i++) {
                Part part = AVAILABLE_PARTS.get(ThreadLocalRandom.current().nextInt(AVAILABLE_PARTS.size()));
                storage.put(part);
                System.out.println("Factory produced: " + part);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        phaser.arriveAndDeregister();
    }
}
