# Class Model Reference

This document explains the classes in the RideWise project, their responsibilities, and their relationships.

---

## 🗂️ Class Diagram (Mermaid)

```mermaid
classDiagram
    class Main {
        -RideMatchingStrategy DEFAULT_MATCHING_STRATEGY
        -FareStrategy DEFAULT_FARE_STRATEGY
        +main(args : String[])
    }

    class Rider {
        -int id
        -String name
        -Location location
    }

    class Driver {
        -int id
        -String name
        -Location currentLocation
        -boolean isActive
        -VehicleType vehicleType
    }

    class Ride {
        -int id
        -double distance
        -Rider rider
        -Driver driver
        -Location sourceLocation
        -Location destinationLocation
        -RideStatus rideStatus
        -FareReceipt fareReceipt
    }

    class FareReceipt {
        -int rideId
        -double amount
        -LocalDateTime generatedAt
    }

    class Location {
        <<enumeration>>
        VIJAYAWADA(0,0)
        GUNTUR(5,3)
        TENALI(3,2)
        ...
        -int x
        -int y
    }

    class VehicleType {
        <<enumeration>>
        BIKE
        AUTO
        CAR
    }

    class RideStatus {
        <<enumeration>>
        ASSIGNED
        COMPLETED
        CANCELLED
    }

    class RideService {
        -RideRepository rideRepository
        -DriverService driverService
        -RiderService riderService
        -RideMatchingStrategy rideMatchingStrategy
        -FareStrategy fareStrategy
        -IdGenerator rideIdGenerator
        +requestRide(riderId, source, destination) Ride
        +completeRide(rideId) void
        +cancelRide(rideId) void
    }

    class DriverService {
        -DriverRepository driverRepository
        -IdGenerator idGenerator
        +createDriver(driver) Driver
        +getDriverById(id) Driver
        +getAvailableDrivers() List~Driver~
        +updateAvailability(driverId, available) void
    }

    class RiderService {
        -RiderRepository riderRepository
        -IdGenerator idGenerator
        +createRider(rider) Rider
        +getRider(id) Rider
    }

    class RideMatchingStrategy {
        <<interface>>
        +findDriver(rider, drivers) Driver
    }

    class FareStrategy {
        <<interface>>
        +calculateFare(ride) double
    }

    Main ..> RideService : configures & invokes
    RideService --> RideMatchingStrategy : delegates matching
    RideService --> FareStrategy : delegates fare calculation
    RideService --> RiderService : validates riders
    RideService --> DriverService : manages driver state
    Ride "1" *-- "1" FareReceipt : composes
    Ride "1" o-- "1" Rider : associates
    Ride "1" o-- "1" Driver : associates
```

---

## 📝 Class Responsibilities

### 1. Model Layer (`com.airtribe.ridewise.model`)

| Class | Type | Responsibility |
|---|---|---|
| **Rider** | Class | Represents the rider domain model containing rider `id`, `name`, and current `Location`. |
| **Driver** | Class | Represents the driver domain model containing driver `id`, `name`, `currentLocation`, `isActive` availability flag, and `VehicleType`. |
| **Ride** | Class | Orchestrates the details of a single booking, including the distance, driver, rider, source, destination, status, and composition of the `FareReceipt`. |
| **FareReceipt** | Class | Read-only receipt detailing the cost of the ride and generation timestamp. |
| **Location** | Enum | Contains pre-populated, immutable coordinates of various Andhra Pradesh cities. |
| **VehicleType** | Enum | Categorizes vehicle modes: `BIKE`, `AUTO`, `CAR`. |
| **RideStatus** | Enum | Defines possible lifecycle states of a ride: `ASSIGNED`, `COMPLETED`, `CANCELLED`. |

### 2. Strategy Layer (`com.airtribe.ridewise.strategy`)

- **RideMatchingStrategy (Interface)**: Defines contract for selecting a driver from a list of available candidates.
  - **NearestDriverStrategy**: Matches driver based on minimum Euclidean distance to rider.
  - **LeastActiveDriverStrategy**: Matches driver based on minimal prior rides count in the system.
- **FareStrategy (Interface)**: Defines contract for calculating ride fares.
  - **DefaultFareStrategy**: Calculates fare based on base rate and per-kilometer rate of vehicle type.
  - **PeakHourFareStrategy**: Decorator wrapper applying a 1.5x multiplier to the base fare during peak hours.

### 3. Service Layer (`com.airtribe.ridewise.service`)

- **RiderService**: Validates and saves riders. Auto-generates IDs using Rider ID Generator.
- **DriverService**: Manages registration, availability toggling, and queries for available drivers.
- **RideService**: Coordinates the booking flow, driver matching, pricing computation, saving active bookings, completing them, or cancelling them.

### 4. Repository Layer (`com.airtribe.ridewise.repository`)

- **RiderRepository / DriverRepository / RideRepository**: Abstractions defining save, query, and lookup operations.
- **RiderRepositoryImpl / DriverRepositoryImpl / RideRepositoryImpl**: Concrete in-memory stores using standard `HashMap` structures.

### 5. Utility Layer (`com.airtribe.ridewise.util`)

- **IdGenerator (Interface)**: Code abstraction defining thread-safe identifier generation.
  - **RiderIdGenerator / DriverIdGenerator / RideIdGenerator**: Generates incrementing IDs starting from 1 for respective entities.
- **DistanceCalculator**: Handles mathematical calculation of distance between locations using Cartesian coordinates.
