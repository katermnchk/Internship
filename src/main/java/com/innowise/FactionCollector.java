package com.innowise;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class FactionCollector implements Runnable {
    private final Faction faction;
    private final BlockingQueue<Part> storage;
    private final Phaser phaser;
    private final int maxParts;

    @Override
    public void run() {
        try {
            phaser.arriveAndAwaitAdvance();

            List<Part> collected = new ArrayList<>();

            for (int i = 0; i < maxParts; i++) {
                Part part = storage.poll(100, TimeUnit.MILLISECONDS);
                if (part == null) {
                    break;
                }
                collected.add(part);
                System.out.println(faction.getName() + " collected: " + part);
            }

            if (!collected.isEmpty()) {
                faction.addParts(collected);
            }

            phaser.arriveAndDeregister();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
