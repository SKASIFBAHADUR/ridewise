package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.VehicleType;

public class DefaultFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        if (ride == null) {
            return 0.0;
        }
        double distance = ride.getDistance();
        double baseFare = 0.0;
        double perUnitFare = 0.0;

        VehicleType vehicleType = (ride.getDriver() != null) ? ride.getDriver().getVehicleType() : VehicleType.CAR;
        if (vehicleType == null) {
            vehicleType = VehicleType.CAR;
        }

        switch (vehicleType) {
            case BIKE:
                baseFare = 20.0;
                perUnitFare = 5.0;
                break;
            case AUTO:
                baseFare = 30.0;
                perUnitFare = 8.0;
                break;
            case CAR:
            default:
                baseFare = 50.0;
                perUnitFare = 12.0;
                break;
        }

        return baseFare + (distance * perUnitFare);
    }
}
