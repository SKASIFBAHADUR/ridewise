package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.exception.ValidationException;
import com.airtribe.ridewise.repository.RideRepository;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.DistanceCalculator;
import com.airtribe.ridewise.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

public class RideService {
    private final RideRepository rideRepository;
    private final DriverService driverService;
    private final RiderService riderService;
    private final RideMatchingStrategy rideMatchingStrategy;
    private final FareStrategy fareStrategy;
    private final IdGenerator rideIdGenerator;

    public RideService(RideRepository rideRepository,
                       DriverService driverService,
                       RiderService riderService,
                       RideMatchingStrategy rideMatchingStrategy,
                       FareStrategy fareStrategy,
                       IdGenerator rideIdGenerator) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
        this.riderService = riderService;
        this.rideMatchingStrategy = rideMatchingStrategy;
        this.fareStrategy = fareStrategy;
        this.rideIdGenerator = rideIdGenerator;
    }

    public Ride requestRide(int riderId, Location source, Location destination) {
        // 1. Validate rider via riderService (throws exception if not found)
        Rider rider = riderService.getRider(riderId);

        // 2 & 3. Validate locations
        if (source == null || destination == null) {
            throw new ValidationException("Source and destination locations must be provided.");
        }
        if (source == destination) {
            throw new ValidationException("Source and destination locations cannot be the same.");
        }

        // 4. Fetch available drivers from driverService
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No available drivers at the moment.");
        }

        // 5. Match driver using the injected RideMatchingStrategy
        Driver matchedDriver = rideMatchingStrategy.findDriver(rider, availableDrivers);
        if (matchedDriver == null) {
            throw new NoDriverAvailableException("No driver could be matched for the ride request.");
        }

        // 6. Create ride
        int rideId = rideIdGenerator.getId();

        // 9. Calculate ride distance
        double distance = DistanceCalculator.calculateDistance(source, destination);

        // 7. Mark driver unavailable
        driverService.updateAvailability(matchedDriver.getId(), false);

        // 8. Create Ride object with ASSIGNED status
        Ride ride = new Ride(rideId, distance, rider, matchedDriver, source, destination, RideStatus.ASSIGNED, null);

        // 10. Calculate fare using the injected FareStrategy
        double fareAmount = fareStrategy.calculateFare(ride);

        // 11. Generate FareReceipt
        FareReceipt fareReceipt = new FareReceipt(rideId, fareAmount, LocalDateTime.now());
        ride.setFareReceipt(fareReceipt);

        // 12. Save ride
        rideRepository.save(ride);

        return ride;
    }

    public void completeRide(int rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride == null) {
            throw new ValidationException("Ride not found with ID: " + rideId);
        }
        if (ride.getRideStatus() != RideStatus.ASSIGNED) {
            throw new ValidationException("Only ASSIGNED rides can be completed. Current status: " + ride.getRideStatus());
        }

        // Set status to COMPLETED
        ride.setRideStatus(RideStatus.COMPLETED);

        // Driver becomes available
        Driver driver = ride.getDriver();
        if (driver != null) {
            driverService.updateAvailability(driver.getId(), true);
            driver.setCurrentLocation(ride.getDestinationLocation()); // Move driver to destination
        }

        rideRepository.save(ride);
    }

    public void cancelRide(int rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride == null) {
            throw new ValidationException("Ride not found with ID: " + rideId);
        }
        if (ride.getRideStatus() != RideStatus.ASSIGNED) {
            throw new ValidationException("Only ASSIGNED rides can be cancelled. Current status: " + ride.getRideStatus());
        }

        // Set status to CANCELLED
        ride.setRideStatus(RideStatus.CANCELLED);

        // Driver becomes available
        Driver driver = ride.getDriver();
        if (driver != null) {
            driverService.updateAvailability(driver.getId(), true);
        }

        rideRepository.save(ride);
    }

    public Ride getRideById(int rideId) {
        Ride ride = rideRepository.findById(rideId);
        if (ride == null) {
            throw new ValidationException("Ride not found with ID: " + rideId);
        }
        return ride;
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }
}
