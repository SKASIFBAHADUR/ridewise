package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Ride;
import java.util.List;

public interface RideRepository {
    void save(Ride ride);
    Ride findById(int id);
    List<Ride> findAll();
}
