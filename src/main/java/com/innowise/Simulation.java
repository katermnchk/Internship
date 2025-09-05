package com.innowise;

import java.util.concurrent.*;

public class Simulation {
    private static final int MAX_PARTS_PER_DAY = 10;
    private static final int MAX_PARTS_PER_FACTION = 5;

    private final BlockingQueue<Part> storage = new LinkedBlockingQueue<>();
    private final Faction world = new Faction("World");
    private final Faction wednesday = new Faction("Wednesday");
    private final ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(3);

    public void run(int days) {
        try {
            for (int day = 1; day <= days; day++) {
                System.out.println("\n---Day " + day + " starts---");

                Phaser phaser = new Phaser(3);

                Future<?> futureProducer = executor.submit(new Factory(storage, phaser, MAX_PARTS_PER_DAY));
                Future<?> futureWorld = executor.submit(new FactionCollector(world, storage, phaser, MAX_PARTS_PER_FACTION));
                Future<?> futureWednesday = executor.submit(new FactionCollector(wednesday, storage, phaser, MAX_PARTS_PER_FACTION));

                try {
                    futureProducer.get(2, TimeUnit.SECONDS);
                    futureWorld.get(2, TimeUnit.SECONDS);
                    futureWednesday.get(2, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    System.out.println("A task took too long and will be cancelled");
                    futureProducer.cancel(true);
                    futureWorld.cancel(true);
                    futureWednesday.cancel(true);
                } catch (ExecutionException e) {
                    System.out.println("Execution exception: " + e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            printResult();
        }
    }

    private void printResult() {
        int worldRobots = world.getRobotsBuilt();
        int wednesdayRobots = wednesday.getRobotsBuilt();

        System.out.println("\n---Result---");
        System.out.println(world.getName() + " built " + worldRobots + " robots");
        System.out.println(wednesday.getName() + " built " + wednesdayRobots + " robots");


        System.out.println();
        if (worldRobots > wednesdayRobots) {
            System.out.println(world.getName() + " wins!");
        } else if (wednesdayRobots > worldRobots) {
            System.out.println(wednesday.getName() + " wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }


}