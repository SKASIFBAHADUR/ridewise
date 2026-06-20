package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Driver;
import java.util.List;

public interface DriverRepository {
    void save(Driver driver);
    Driver findById(int id);
    List<Driver> findAll();
}
