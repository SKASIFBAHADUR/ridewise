package com.airtribe.ridewise.inputvalidator;

import com.airtribe.ridewise.exception.ValidationException;
import com.airtribe.ridewise.model.Rider;

public class RiderInputValidator {

    private RiderInputValidator() {}

    public static void validate(Rider rider) {
        if (rider.getName() == null || rider.getName().trim().isEmpty()) {
            throw new ValidationException("Rider name must not be empty.");
        }
        if (rider.getLocation() == null) {
            throw new ValidationException("Rider location must not be null.");
        }
    }

    public static void validateId(int id) {
        if (id <= 0) {
            throw new ValidationException("Rider ID must be a positive integer.");
        }
    }
}
