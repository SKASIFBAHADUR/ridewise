package com.airtribe.ridewise.model;

public enum VehicleType {
    BIKE,
    CAR,
    AUTO;

    public static VehicleType getBike(){
        return BIKE;
    }

    public static VehicleType getCar(){
        return CAR;
    }

    public static VehicleType getAuto(){
        return AUTO;
    }
}
