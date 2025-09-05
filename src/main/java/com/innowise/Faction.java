package com.innowise;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Faction {
    @Getter
    private final String name;
    private final List<Part> parts = new ArrayList<>();
    private int robotsCount = 0;

    public Faction(String name) {
        this.name = name;
    }

    public void addParts(List<Part> newParts) {
        parts.addAll(newParts);
        while (canBuildRobot()) {
            buildRobot();
        }
    }

    private boolean canBuildRobot() {
        int head = 0, torso = 0, hand = 0, feet = 0;

        for (Part part : parts) {
            switch (part) {
                case HEAD -> head++;
                case TORSO -> torso++;
                case HAND -> hand++;
                case FEET -> feet++;
            }
        }
        return head >= 1 && torso >= 1 && hand >= 2 && feet >= 2;
    }

    private void buildRobot() {
        removeParts(Part.HEAD, 1);
        removeParts(Part.TORSO, 1);
        removeParts(Part.HAND, 2);
        removeParts(Part.FEET, 2);
        robotsCount++;
    }

    private void removeParts(Part type, int count) {
        for (int i = 0; i < count; i++) {
            parts.remove(type);
        }
    }

    public int getRobotsBuilt() {
        return robotsCount;
    }
}
