package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.repository.RideRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {
    private final RideRepository rideRepository;

    public LeastActiveDriverStrategy(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        if (drivers == null || drivers.isEmpty()) {
            return null;
        }

        // Count rides assigned to each driver from the ride repository
        Map<Integer, Integer> rideCounts = new HashMap<>();
        List<Ride> allRides = rideRepository.findAll();
        for (Ride ride : allRides) {
            if (ride.getDriver() != null) {
                int driverId = ride.getDriver().getId();
                rideCounts.put(driverId, rideCounts.getOrDefault(driverId, 0) + 1);
            }
        }

        Driver leastActiveDriver = null;
        int minRides = Integer.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.isActive()) {
                int count = rideCounts.getOrDefault(driver.getId(), 0);
                if (count < minRides) {
                    minRides = count;
                    leastActiveDriver = driver;
                }
            }
        }

        return leastActiveDriver;
    }
}
