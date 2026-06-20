package com.airtribe.ridewise;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.repository.DriverRepository;
import com.airtribe.ridewise.repository.DriverRepositoryImpl;
import com.airtribe.ridewise.repository.RideRepository;
import com.airtribe.ridewise.repository.RideRepositoryImpl;
import com.airtribe.ridewise.repository.RiderRepository;
import com.airtribe.ridewise.repository.RiderRepositoryImpl;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.DriverIdGenerator;
import com.airtribe.ridewise.util.IdGenerator;
import com.airtribe.ridewise.util.RideIdGenerator;
import com.airtribe.ridewise.util.RiderIdGenerator;

import java.util.List;
import java.util.Scanner;

public class Main {
    // Composition Root: Predefined default strategies
    private static final RideMatchingStrategy DEFAULT_MATCHING_STRATEGY = new NearestDriverStrategy();
    private static final FareStrategy DEFAULT_FARE_STRATEGY = new DefaultFareStrategy();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RiderRepository riderRepository = new RiderRepositoryImpl();
        IdGenerator riderIdGenerator = new RiderIdGenerator();
        RiderService riderService = new RiderService(riderRepository, riderIdGenerator);

        DriverRepository driverRepository = new DriverRepositoryImpl();
        IdGenerator driverIdGenerator = new DriverIdGenerator();
        DriverService driverService = new DriverService(driverRepository, driverIdGenerator);

        RideRepository rideRepository = new RideRepositoryImpl();
        IdGenerator rideIdGenerator = new RideIdGenerator();

        // Composition Root: strategies configured during application startup
        RideService rideService = new RideService(
                rideRepository,
                driverService,
                riderService,
                DEFAULT_MATCHING_STRATEGY,
                DEFAULT_FARE_STRATEGY,
                rideIdGenerator
        );

        // Pre-populating some drivers and riders for quick testing
        driverService.createDriver(new Driver(0, "bhanu", Location.AMALAPURAM, true, VehicleType.BIKE));
        driverService.createDriver(new Driver(0, "roshan", Location.ADONI, true, VehicleType.AUTO));
        driverService.createDriver(new Driver(0, "sohel", Location.ANANTAPUR, true, VehicleType.CAR));
        driverService.createDriver(new Driver(0, "farooq", Location.BHIMAVARAM, true, VehicleType.BIKE));
        driverService.createDriver(new Driver(0, "vinod", Location.CHILAKALURIPET, true, VehicleType.AUTO));
        driverService.createDriver(new Driver(0, "dhanush", Location.ELURU, true, VehicleType.CAR));

        riderService.createRider(new Rider(0, "Bahadur", Location.VIJAYAWADA));

        System.out.println("=== Welcome to RideWise Application ===");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add Rider");
            System.out.println("2. Add Driver");
            System.out.println("3. View Available Drivers");
            System.out.println("4. Request Ride");
            System.out.println("5. Complete Ride");
            System.out.println("6. View Rides");
            System.out.println("7. Exit");
            System.out.print("Choose an option (1-7): ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } else {
                System.out.println("Invalid option. Please enter a number.");
                scanner.next();
                continue;
            }

            try {
                switch (choice) {
                    case 1: {
                        System.out.print("Enter Rider Name: ");
                        String name = scanner.nextLine();
                        System.out.println("Select Rider current location:");
                        Location location = selectLocation(scanner);
                        Rider rider = new Rider(0, name, location);
                        Rider created = riderService.createRider(rider);
                        System.out.println("Rider added successfully! ID: " + created.getId() + ", Name: " + created.getName());
                        break;
                    }
                    case 2: {
                        System.out.print("Enter Driver Name: ");
                        String name = scanner.nextLine();
                        System.out.println("Select Driver location:");
                        Location location = selectLocation(scanner);
                        System.out.println("Select vehicle type:");
                        VehicleType vehicleType = selectVehicleType(scanner);
                        System.out.print("Is driver active/available? (1. Yes, 2. No): ");
                        boolean isActive = true;
                        if (scanner.hasNextInt()) {
                            int activeChoice = scanner.nextInt();
                            scanner.nextLine();
                            isActive = (activeChoice == 1);
                        } else {
                            scanner.next();
                        }
                        Driver driver = new Driver(0, name, location, isActive, vehicleType);
                        Driver created = driverService.createDriver(driver);
                        System.out.println("Driver added successfully! ID: " + created.getId() + ", Name: " + created.getName());
                        break;
                    }
                    case 3: {
                        List<Driver> availableDrivers = driverService.getAvailableDrivers();
                        if (availableDrivers.isEmpty()) {
                            System.out.println("No drivers are currently available.");
                        } else {
                            System.out.println("--- Available Drivers ---");
                            for (Driver d : availableDrivers) {
                                System.out.println("ID: " + d.getId() + " | Name: " + d.getName() + " | Location: " + d.getCurrentLocation() + " | Vehicle: " + d.getVehicleType());
                            }
                        }
                        break;
                    }
                    case 4: {
                        System.out.print("Enter Rider ID: ");
                        int riderId = -1;
                        if (scanner.hasNextInt()) {
                            riderId = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            scanner.next();
                            System.out.println("Invalid ID format.");
                            break;
                        }

                        Rider rider = riderService.getRider(riderId);

                        System.out.println("Select Pickup Location (Source):");
                        Location source = selectLocation(scanner);
                        System.out.println("Select Destination Location:");
                        Location dest = selectLocation(scanner);

                        // Request the ride using configured strategies
                        Ride ride = rideService.requestRide(riderId, source, dest);
                        System.out.println("Ride requested and ASSIGNED successfully!");
                        System.out.println("Ride details: " + ride);
                        break;
                    }
                    case 5: {
                        System.out.print("Enter Ride ID to complete: ");
                        int rideId = -1;
                        if (scanner.hasNextInt()) {
                            rideId = scanner.nextInt();
                            scanner.nextLine();
                        } else {
                            scanner.next();
                            System.out.println("Invalid ID format.");
                            break;
                        }
                        rideService.completeRide(rideId);
                        System.out.println("Ride " + rideId + " marked as COMPLETED. Driver is now available.");
                        break;
                    }
                    case 6: {
                        List<Ride> allRides = rideService.getAllRides();
                        if (allRides.isEmpty()) {
                            System.out.println("No rides found in the system.");
                        } else {
                            System.out.println("--- All Rides ---");
                            for (Ride r : allRides) {
                                System.out.println(r);
                            }
                        }
                        break;
                    }
                    case 7: {
                        System.out.println("Thank you for using RideWise. Goodbye!");
                        scanner.close();
                        System.exit(0);
                    }
                    default:
                        System.out.println("Invalid menu choice. Please select 1 to 7.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static Location selectLocation(Scanner scanner) {
        Location[] locations = Location.values();
        while (true) {
            System.out.println("Choose location index:");
            for (int i = 0; i < locations.length; i++) {
                System.out.printf("%2d. %s (%d, %d)%n", (i + 1), locations[i].name(), locations[i].getX(), locations[i].getY());
            }
            System.out.print("Enter number (1-" + locations.length + "): ");
            if (scanner.hasNextInt()) {
                int selection = scanner.nextInt();
                scanner.nextLine();
                if (selection >= 1 && selection <= locations.length) {
                    return locations[selection - 1];
                }
            } else {
                scanner.next(); // clear invalid input
            }
            System.out.println("Invalid location choice. Try again.");
        }
    }

    private static VehicleType selectVehicleType(Scanner scanner) {
        VehicleType[] types = VehicleType.values();
        while (true) {
            System.out.println("Choose vehicle type index:");
            for (int i = 0; i < types.length; i++) {
                System.out.printf("%d. %s%n", (i + 1), types[i].name());
            }
            System.out.print("Enter number (1-" + types.length + "): ");
            if (scanner.hasNextInt()) {
                int selection = scanner.nextInt();
                scanner.nextLine();
                if (selection >= 1 && selection <= types.length) {
                    return types[selection - 1];
                }
            } else {
                scanner.next(); // clear invalid input
            }
            System.out.println("Invalid vehicle type choice. Try again.");
        }
    }
}