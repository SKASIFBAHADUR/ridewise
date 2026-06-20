package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import java.time.LocalDateTime;

public class PeakHourFareStrategy implements FareStrategy {
    private final FareStrategy baseStrategy;
    private final double multiplier;
    private final Integer overrideHour;

    public PeakHourFareStrategy(FareStrategy baseStrategy) {
        this.baseStrategy = baseStrategy;
        this.multiplier = 1.5;
        this.overrideHour = null;
    }

    public PeakHourFareStrategy(FareStrategy baseStrategy, double multiplier) {
        this.baseStrategy = baseStrategy;
        this.multiplier = multiplier;
        this.overrideHour = null;
    }

    public PeakHourFareStrategy(FareStrategy baseStrategy, double multiplier, int overrideHour) {
        this.baseStrategy = baseStrategy;
        this.multiplier = multiplier;
        this.overrideHour = overrideHour;
    }

    @Override
    public double calculateFare(Ride ride) {
        double baseFare = baseStrategy.calculateFare(ride);
        if (isPeakHour()) {
            return baseFare * multiplier;
        }
        return baseFare;
    }

    private boolean isPeakHour() {
        int hour = (overrideHour != null) ? overrideHour : LocalDateTime.now().getHour();
        // Peak hours: 8:00 AM to 11:00 AM, and 5:00 PM to 9:00 PM
        return (hour >= 8 && hour < 11) || (hour >= 17 && hour < 21);
    }
}
