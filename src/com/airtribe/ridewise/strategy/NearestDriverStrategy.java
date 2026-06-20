package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.DistanceCalculator;
import java.util.List;

public class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (rider == null || drivers == null || drivers.isEmpty()) {
            return null;
        }
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;
        for (Driver driver : drivers) {
            if (driver.isActive()) {
                double distance = DistanceCalculator.calculateDistance(rider.getLocation(), driver.getCurrentLocation());
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestDriver = driver;
                }
            }
        }
        return nearestDriver;
    }
}
