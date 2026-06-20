package com.airtribe.ridewise.inputvalidator;

import com.airtribe.ridewise.exception.ValidationException;
import com.airtribe.ridewise.model.Driver;

public class DriverInputValidator {

    private DriverInputValidator() {}

    public static void validate(Driver driver) {
        if (driver.getName() == null || driver.getName().trim().isEmpty()) {
            throw new ValidationException("Driver name must not be empty.");
        }
        if (driver.getCurrentLocation() == null) {
            throw new ValidationException("Driver location must not be null.");
        }
        if (driver.getVehicleType() == null) {
            throw new ValidationException("Driver vehicle type must not be null.");
        }
    }

    public static void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("Driver ID must be a positive integer.");
        }
    }
}
