package com.airtribe.ridewise.repository;

import com.airtribe.ridewise.model.Rider;
import java.util.List;

public interface RiderRepository {
    void save(Rider rider);
    Rider findById(int id);
    List<Rider> findAll();
}
