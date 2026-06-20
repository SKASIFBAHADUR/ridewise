# Implemented Requirements Summary

This document outlines the requirements implemented in the RideWise ride-sharing application.

## 📋 Functional Requirements

### 1. Rider Management
- **Rider Registration**: Allows registering riders. Each rider has an auto-generated unique ID, a name, and a current location chosen from the `Location` enum.
- **Rider Validation**: Validates that rider name is not empty and location is valid.
- **Rider Query**: Retrieve riders by ID to verify their existence before starting a booking.

### 2. Driver Management
- **Driver Registration**: Register drivers with a name, starting location, vehicle type (`BIKE`, `AUTO`, `CAR`), and availability status.
- **Availability Updates**: Dynamically changes a driver's status to unavailable when matched to a ride, and restores availability when the ride is completed or cancelled.
- **List Available Drivers**: Real-time display of drivers who are active/available.

### 3. Ride Lifecycle Management
- **Request Ride**: Enables a rider to request a ride from a pickup (source) location to a destination location.
- **Driver Matching**: Automatically assigns a driver using a configured matching algorithm.
- **Fare Receipt Generation**: Generates a receipt containing the ride ID, total fare, and generation timestamp immediately upon ride assignment.
- **Complete Ride**: Marks an active ride as `COMPLETED`. The driver's location is moved to the destination, and their availability is set back to `true`.
- **Cancel Ride**: Marks an active ride as `CANCELLED`, freeing up the driver.
- **View Rides**: Allows the user to view all requested rides in the system along with their current status.

### 4. Location & Distance
- **Coordinate Enum Constraint**: Uses only the pre-defined `Location` enum values which map to fixed Cartesian coordinates (e.g., `VIJAYAWADA(0,0)`, `GUNTUR(5,3)`). No custom location models are created.
- **Distance Calculation**: Uses Euclidean distance formulation: $\sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$ to determine distances between sources, destinations, and driver locations.

### 5. Strategy Implementation
- **Pluggable Ride Matching**:
  - `NearestDriverStrategy`: Matches the rider with the closest available driver.
  - `LeastActiveDriverStrategy`: Matches the rider with the driver who has the lowest count of assigned rides.
- **Pluggable Fare Calculation**:
  - `DefaultFareStrategy`: Computes base fare plus vehicle-type per-kilometer charges.
  - `PeakHourFareStrategy`: Dynamically increases base rate by a 1.5x multiplier during peak hours.

### 6. Exception Handling
- **NoDriverAvailableException**: Thrown when a ride request fails because no drivers are currently available or no drivers match.
- **RiderNotFoundException & DriverNotFoundException**: Thrown when lookups for corresponding entities by ID fail.
- **ValidationException**: Thrown when validation checks fail (e.g. source and destination are identical, blank names, etc.).

---

## ⚙️ Non-Functional Requirements

| Requirement | Description | Implementation Detail |
|---|---|---|
| **Low Coupling** | Services do not interact with repositories or databases directly, utilizing abstraction interfaces. | Services depend on interface contracts (`RiderRepository`, `RideRepository`). |
| **High Cohesion** | Classes perform only their core domain functions. | `DistanceCalculator` only calculates distance; `IdGenerator` only manages IDs. |
| **Extensibility** | Pricing and matching logic should be easy to extend. | Implemented via the Strategy Pattern, allowing clean extensions without modifying orchestrating services. |
| **Startup Configuration** | Strategy composition is determined during application startup. | Configured inside `Main.java` (Composition Root) instead of requesting strategy choices from user. |
