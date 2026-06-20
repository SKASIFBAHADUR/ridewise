package com.airtribe.ridewise.util;

import com.airtribe.ridewise.model.Location;

public class DistanceCalculator {

    private DistanceCalculator() {
        // Utility class — no instantiation
    }

    /**
     * Calculates the Euclidean distance between two Location enum values
     * using their embedded x, y coordinates.
     * Formula: distance = sqrt((x2-x1)^2 + (y2-y1)^2)
     */
    public static double calculateDistance(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            throw new IllegalArgumentException("Locations must not be null");
        }
        int dx = loc2.getX() - loc1.getX();
        int dy = loc2.getY() - loc1.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
