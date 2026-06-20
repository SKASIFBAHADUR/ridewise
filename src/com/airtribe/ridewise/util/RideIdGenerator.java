package com.airtribe.ridewise.util;

public class RideIdGenerator implements IdGenerator {
    private static int RIDE_COUNT = 1;

    @Override
    public int getId() {
        return RIDE_COUNT++;
    }

    @Override
    public int getValue() {
        return RIDE_COUNT;
    }
}
