package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Rider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiderRepositoryImpl implements RiderRepository {
    private final Map<Integer, Rider> store = new HashMap<>();

    @Override
    public void save(Rider rider) {
        store.put(rider.getId(), rider);
    }

    @Override
    public Rider findById(int id) {
        return store.get(id);
    }

    @Override
    public List<Rider> findAll() {
        return new ArrayList<>(store.values());
    }
}
