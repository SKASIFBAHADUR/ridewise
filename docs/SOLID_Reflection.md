# SOLID Principles Reflection

This document details the reflection on how **SOLID** software design principles are implemented within the RideWise project.

---

## ☕ 1. Single Responsibility Principle (SRP)
> "A class should have one, and only one, reason to change."

- **Implementation**:
  - **Repositories**: Classes like `DriverRepositoryImpl` are strictly responsible for storing and retrieving `Driver` domain objects. They contain no registration logic, coordinate conversions, or validation logic.
  - **Validators**: The validation logic is encapsulated entirely within separate classes like `DriverInputValidator` and `RiderInputValidator`, protecting domain models and services from changing when validation requirements change.
  - **DistanceCalculator**: Is single-purposed, performing pure mathematical calculations based on the `Location` coordinates.

---

## 🚪 2. Open-Closed Principle (OCP)
> "Software entities should be open for extension, but closed for modification."

- **Implementation**:
  - **Strategy Pattern**: The core ride orchestrator, `RideService`, is completely closed to modification when introducing new pricing algorithms or matching schemes.
  - **Example**: If we want to add a `DiscountedFareStrategy` or a `VIPDriverMatchingStrategy`, we do not need to modify a single line of code in `RideService`. We simply write a new class implementing the respective `FareStrategy` or `RideMatchingStrategy` interface, and inject it at startup.

---

## 🔀 3. Liskov Substitution Principle (LSP)
> "Subtypes must be substitutable for their base types without altering the correctness of the program."

- **Implementation**:
  - **Strategy Interoperability**: Every class implementing `RideMatchingStrategy` (`NearestDriverStrategy`, `LeastActiveDriverStrategy`) behaves identically regarding input parameters and return types. 
  - **Repository Interoperability**: `RiderRepositoryImpl` implements all methods defined in the `RiderRepository` contract. The application can swap this memory-based storage implementation for a database implementation (e.g., `SqlRiderRepository`) without affecting the execution flow of the services.

---

## 📐 4. Interface Segregation Principle (ISP)
> "Clients should not be forced to depend on methods they do not use."

- **Implementation**:
  - **Granular Interfaces**: Instead of declaring a single, large, monolithic interface representing the entire ride business logic (e.g., `RideSystemOperations`), we break down the contracts into small, cohesive, single-method interfaces.
  - **Example**: 
    - `RideMatchingStrategy` only declares `findDriver()`.
    - `FareStrategy` only declares `calculateFare()`.
    - `IdGenerator` only declares `getId()`.

---

## 🔌 5. Dependency Inversion Principle (DIP)
> "Depend upon abstractions, not concretions."

- **Implementation**:
  - **Constructor Injection**: High-level modules like `RideService` do not instantiate their repository dependencies, ID generators, or strategy handlers directly.
  - **Example**:
    ```java
    public RideService(RideRepository rideRepository,
                       DriverService driverService,
                       RiderService riderService,
                       RideMatchingStrategy rideMatchingStrategy,
                       FareStrategy fareStrategy,
                       IdGenerator rideIdGenerator) { ... }
    ```
    This allows mock implementations of these interfaces to be passed during unit testing, creating a decoupled, highly testable application architecture.
