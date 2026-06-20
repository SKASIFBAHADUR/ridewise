package com.airtribe.ridewise.util;

public class RiderIdGenerator implements IdGenerator {
    private static int RIDER_COUNT = 1;

    @Override
    public int getId() {
        return RIDER_COUNT++;
    }

    @Override
    public int getValue() {
        return RIDER_COUNT;
    }
}
