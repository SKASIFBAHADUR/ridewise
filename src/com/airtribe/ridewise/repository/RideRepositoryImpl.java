package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Ride;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideRepositoryImpl implements RideRepository {
    private final Map<Integer, Ride> store = new HashMap<>();

    @Override
    public void save(Ride ride) {
        store.put((int) ride.getId(), ride);
    }

    @Override
    public Ride findById(int id) {
        return store.get(id);
    }

    @Override
    public List<Ride> findAll() {
        return new ArrayList<>(store.values());
    }
}
