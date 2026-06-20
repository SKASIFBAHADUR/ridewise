package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.exception.DriverNotFoundException;
import com.airtribe.ridewise.inputvalidator.DriverInputValidator;
import com.airtribe.ridewise.repository.DriverRepository;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private final DriverRepository driverRepository;
    private final IdGenerator idGenerator;

    public DriverService(DriverRepository driverRepository, IdGenerator idGenerator) {
        this.driverRepository = driverRepository;
        this.idGenerator = idGenerator;
    }

    public Driver createDriver(Driver driver) {
        DriverInputValidator.validate(driver);
        driver.setId(idGenerator.getId());
        driverRepository.save(driver);
        return driver;
    }

    public Driver registerDriver(Driver driver) {
        return createDriver(driver);
    }

    public Driver getDriverById(int id) {
        DriverInputValidator.validateId(id);
        Driver driver = driverRepository.findById(id);
        if (driver == null) {
            throw new DriverNotFoundException("driver not found");
        }
        return driver;
    }

    public List<Driver> getAvailableDrivers() {
        List<Driver> available = new ArrayList<>();
        for (Driver d : driverRepository.findAll()) {
            if (d.isActive()) {
                available.add(d);
            }
        }
        return available;
    }

    public void updateAvailability(int driverId, boolean available) {
        Driver driver = getDriverById(driverId);
        driver.setActive(available);
        driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}
