package com.airtribe.ridewise.model;

import java.time.LocalDateTime;

public class FareReceipt {
    private long rideId;
    private double amount;
    private LocalDateTime generatedAt;

    public FareReceipt(long rideId, double amount, LocalDateTime generatedAt) {
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = generatedAt;
    }

    public long getRideId() {
        return rideId;
    }

    public void setRideId(long rideId) {
        this.rideId = rideId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "FareReceipt{" +
                "rideId=" + rideId +
                ", amount=₹" + String.format("%.2f", amount) +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
