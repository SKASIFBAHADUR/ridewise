package com.airtribe.ridewise.model;

public class Ride {
    private long id;
    private double distance;
    private Rider rider;
    private Driver driver;
    private Location sourceLocation;
    private Location destinationLocation;
    private RideStatus rideStatus;
    private FareReceipt fareReceipt;

    public Ride(long id, double distance, Rider rider, Driver driver,
                Location sourceLocation, Location destinationLocation,
                RideStatus rideStatus, FareReceipt fareReceipt) {
        this.id = id;
        this.distance = distance;
        this.rider = rider;
        this.driver = driver;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.rideStatus = rideStatus;
        this.fareReceipt = fareReceipt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public Rider getRider() { return rider; }
    public void setRider(Rider rider) { this.rider = rider; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public Location getSourceLocation() { return sourceLocation; }
    public void setSourceLocation(Location sourceLocation) { this.sourceLocation = sourceLocation; }

    public Location getDestinationLocation() { return destinationLocation; }
    public void setDestinationLocation(Location destinationLocation) { this.destinationLocation = destinationLocation; }

    public RideStatus getRideStatus() { return rideStatus; }
    public void setRideStatus(RideStatus rideStatus) { this.rideStatus = rideStatus; }

    public FareReceipt getFareReceipt() { return fareReceipt; }
    public void setFareReceipt(FareReceipt fareReceipt) { this.fareReceipt = fareReceipt; }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", distance=" + String.format("%.2f km", distance) +
                ", rider=" + (rider != null ? rider.getName() : "N/A") +
                ", driver=" + (driver != null ? driver.getName() : "N/A") +
                ", from=" + sourceLocation +
                ", to=" + destinationLocation +
                ", status=" + rideStatus +
                ", receipt=" + fareReceipt +
                '}';
    }
}
