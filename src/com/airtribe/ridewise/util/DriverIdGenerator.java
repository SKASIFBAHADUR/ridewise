package com.airtribe.ridewise.util;

public class DriverIdGenerator implements IdGenerator {
    private static int DRIVER_COUNT = 1;

    @Override
    public int getId() {
        return DRIVER_COUNT++;
    }

    @Override
    public int getValue() {
        return DRIVER_COUNT;
    }
}
