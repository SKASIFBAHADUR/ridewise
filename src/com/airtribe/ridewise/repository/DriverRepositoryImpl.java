package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverRepositoryImpl implements DriverRepository {
    private final Map<Integer, Driver> store = new HashMap<>();

    @Override
    public void save(Driver driver) {
        store.put(driver.getId(), driver);
    }

    @Override
    public Driver findById(int id) {
        return store.get(id);
    }

    @Override
    public List<Driver> findAll() {
        return new ArrayList<>(store.values());
    }
}
