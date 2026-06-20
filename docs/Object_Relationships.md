# Object Relationships

This document details the structural relationships between domain models, services, and repositories in the RideWise application.

---

## 🔗 Domain Model Relationships

| Association | Type of Relationship | Multiplicity | Description |
|---|---|---|---|
| **Rider ↔ Ride** | Association | `1 ── 0..*` | A `Rider` can book multiple rides over time. A `Ride` belongs to exactly one `Rider`. |
| **Driver ↔ Ride** | Association | `1 ── 0..*` | A `Driver` can be assigned to multiple rides (sequentially). A `Ride` is serviced by exactly one `Driver`. |
| **Ride ↔ FareReceipt**| Composition | `1 ── 1` | A `Ride` contains exactly one `FareReceipt`. The receipt cannot exist without its corresponding ride. If the ride is deleted, the receipt is destroyed. |

### Diagrammatic View
```
  [Rider] (1) ◄────────── (0..*) [Ride] (0..*) ──────────► (1) [Driver]
                                    │ (1)
                                    └───► [FareReceipt] (Composition)
```

---

## 🏛️ Service Layer Relationships

```
              ┌──────────────┐
              │     Main     │
              └──────┬───────┘
                     │
                     ▼
              ┌──────────────┐
              │ RideService  │
              └─┬──────────┬─┘
                │          │
        ┌───────┘          └───────┐
        ▼                          ▼
┌──────────────┐            ┌──────────────┐
│ RiderService │            │DriverService │
└──────────────┘            └──────────────┘
```

- **Description**:
  - `RideService` acts as the primary orchestrator. To create a ride, it relies on `RiderService` to validate and retrieve the rider.
  - It relies on `DriverService` to query available drivers and toggle driver availability.
  - This composition structure avoids cyclic dependencies between services (e.g., `DriverService` has no dependency on `RideService`).

---

## 🗄️ Repository Layer Relationships

| Service | Repository Used | Purpose |
|---|---|---|
| **RiderService** | `RiderRepository` | To persist rider registration details and fetch riders during booking. |
| **DriverService** | `DriverRepository` | To persist driver registration details and fetch lists of available/active drivers. |
| **RideService** | `RideRepository` | To save active bookings and retrieve them for completions or cancellations. |

- **Design Aspect**:
  - Each repository is isolated from the others.
  - The repository layer exposes no business logic and works purely with raw data transactions, keeping coupling low.
