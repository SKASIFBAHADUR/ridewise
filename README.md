# RideWise - LLD Ride-Sharing System

RideWise is a console-based Ride-Sharing (Uber/Ola style) LLD application built using Java. It showcases modern software engineering design principles, including **Object-Oriented Programming (OOP)**, **SOLID Principles**, and the **Strategy Pattern**. The application handles rider/driver registration, dynamic driver matching, ride distance/fare calculation, and ride lifecycle tracking.

---

## 🚀 Features

- **Rider Registration**: Add riders with a unique, auto-generated ID and set their initial location.
- **Driver Registration**: Add drivers with locations, vehicle types (BIKE, AUTO, CAR), and availability status.
- **View Available Drivers**: List all active drivers currently available for matching.
- **Request Ride**: Matches riders with drivers using a pluggable allocation strategy and calculates the receipt using a pricing strategy.
- **Complete Ride**: Finishes active rides, moves drivers to the destination, and changes their status back to active.
- **Cancel Ride**: Cancel an assigned ride, freeing the driver.
- **Ride Lifecycle Tracking**: Status transitions: `ASSIGNED` ➔ `COMPLETED` or `CANCELLED`.
- **Pre-populated Seed Data**: The app launches with a set of pre-configured drivers and riders for immediate testing.

---

## 🛠️ Project Structure

The project has been organized into clear, single-responsibility packages under the `com.airtribe.ridewise` namespace:

```
src/
└── com/
    └── airtribe/
        └── ridewise/
            ├── Main.java              # Composition root and interactive console menu
            ├── model/                 # Domain models and enums
            │   ├── Driver.java
            │   ├── Rider.java
            │   ├── Ride.java
            │   ├── FareReceipt.java
            │   ├── Location.java       # Coordinate-based locations
            │   ├── RideStatus.java     # ASSIGNED, COMPLETED, CANCELLED
            │   └── VehicleType.java    # BIKE, AUTO, CAR
            ├── strategy/              # Pluggable algorithms (Strategy Pattern)
            │   ├── RideMatchingStrategy.java
            │   ├── NearestDriverStrategy.java
            │   ├── LeastActiveDriverStrategy.java
            │   ├── FareStrategy.java
            │   ├── DefaultFareStrategy.java
            │   └── PeakHourFareStrategy.java
            ├── service/               # Application service layer / orchestrators
            │   ├── RiderService.java
            │   ├── DriverService.java
            │   └── RideService.java
            ├── exception/             # Custom domain exceptions
            │   ├── NoDriverAvailableException.java
            │   ├── DriverNotFoundException.java
            │   ├── RiderNotFoundException.java
            │   └── ValidationException.java
            └── util/                  # ID generators and utility classes
                ├── IdGenerator.java
                ├── DriverIdGenerator.java
                ├── RiderIdGenerator.java
                ├── RideIdGenerator.java
                └── DistanceCalculator.java
docs/
├── Requirements.md
├── Class_Model.md
├── SOLID_Reflection.md
└── Object_Relationships.md
```

---

## 🧩 OOP Concepts Used

1. **Encapsulation**: Domain models like `Rider` and `Driver` keep their state private and expose behavior/data via getters and setters, maintaining strict boundaries.
2. **Polymorphism**: Used heavily in the implementation of the Strategy Pattern. Dynamic dispatch is used to run the appropriate `RideMatchingStrategy` and `FareStrategy` implementations at runtime.
3. **Abstraction**: Small, focused interfaces define the behavior contract (e.g., `RideMatchingStrategy`, `FareStrategy`, `IdGenerator`, `RiderRepository`) without exposing underlying implementation logic.
4. **Composition over Inheritance**: Services (`RideService`) compose repositories and strategies rather than extending them, reducing tight coupling.

---

## 📐 SOLID Principles Used

- **Single Responsibility Principle (SRP)**: Each class has one job. For example, `DistanceCalculator` is only responsible for calculating distance between locations, and `DriverService` only manages driver registration and state.
- **Open-Closed Principle (OCP)**: Adding new matching algorithms (e.g., `LeastActiveDriverStrategy`) or fare calculations (e.g., `PeakHourFareStrategy`) is done by adding new classes implementing the interface, without modifying the core `RideService`.
- **Liskov Substitution Principle (LSP)**: All strategy implementations can substitute their interfaces seamlessly. For example, `NearestDriverStrategy` can be swapped with `LeastActiveDriverStrategy` at startup without breaking `RideService`.
- **Interface Segregation Principle (ISP)**: Interfaces are lean and specialized. Instead of one monolithic service interface, we use distinct interfaces like `RideMatchingStrategy` and `FareStrategy`.
- **Dependency Inversion Principle (DIP)**: `RideService` depends strictly on abstractions (`RideMatchingStrategy`, `FareStrategy`, `RideRepository`, `IdGenerator`) rather than concrete classes. Concrete classes are injected at runtime via constructor injection.

---

## 🎯 Strategy Pattern Explanation

The application delegates algorithmic decisions (matching a driver and calculating fares) to separate strategy families.

1. **Ride Matching Strategy**:
   - `RideMatchingStrategy`: The interface defining `findDriver()`.
   - `NearestDriverStrategy`: Matches the rider with the closest available driver using Euclidean distance.
   - `LeastActiveDriverStrategy`: Matches the rider with the available driver who has completed the fewest rides (using `RideRepository` counts) to balance driver utilization.

2. **Fare Calculation Strategy**:
   - `FareStrategy`: The interface defining `calculateFare()`.
   - `DefaultFareStrategy`: Computes base fare plus distance-based rates depending on the vehicle type (BIKE, AUTO, CAR).
   - `PeakHourFareStrategy`: Acts as a decorator wrapping a base `FareStrategy`, adding a 1.5x multiplier during peak hours (8 AM - 11 AM, 5 PM - 9 PM).

---

## 🚀 How to Run

### Prerequisites
- JDK 8 or higher installed.

### Compilation
Compile the project to the `out` directory:
```bash
# On Windows (PowerShell)
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Out-File -FilePath sources.txt -Encoding ASCII; javac -d out -sourcepath src "@sources.txt"; Remove-Item sources.txt
```

### Execution
Run the console menu app:
```bash
java -cp out com.airtribe.ridewise.Main
```

---

## 📊 Sample Workflow

1. **Add Rider**: Create a rider named "Alice" at `VIJAYAWADA` (ID: 2).
2. **View Available Drivers**: See the pre-populated active drivers (e.g., `dhanush` at `ELURU`).
3. **Request Ride**: Book a ride for Bahadur (ID: 1) from `VIJAYAWADA` to `GUNTUR`. The nearest driver is automatically matched, fare is calculated, and a `FareReceipt` is printed.
4. **Complete Ride**: Select the ride ID (e.g., 1) to mark the ride complete. The driver becomes active again and moves to `GUNTUR`.
5. **View Rides**: See all current and past rides in the system with their statuses.

---

## 🔮 Future Improvements

- **Database Persistence**: Replace the in-memory repositories with JDBC/JPA database connectors.
- **Dynamic Pricing (Surge)**: Introduce traffic-based pricing or supply-demand pricing strategies.
- **Rider/Driver Ratings**: Implement a feedback system where riders and drivers can rate each other.
- **Real-Time Location Updates**: Support simulation of moving drivers and live distance tracking.
