package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.exception.RiderNotFoundException;
import com.airtribe.ridewise.inputvalidator.RiderInputValidator;
import com.airtribe.ridewise.repository.RiderRepository;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.List;

public class RiderService {
    private final RiderRepository riderRepository;
    private final IdGenerator idGenerator;

    public RiderService(RiderRepository riderRepository, IdGenerator idGenerator) {
        this.riderRepository = riderRepository;
        this.idGenerator = idGenerator;
    }

    public Rider createRider(Rider rider) {
        RiderInputValidator.validate(rider);
        rider.setId(idGenerator.getId());
        riderRepository.save(rider);
        return rider;
    }

    public Rider getRider(int id) {
        Rider rider = riderRepository.findById(id);
        if (rider == null) {
            throw new RiderNotFoundException("rider not found");
        }
        return rider;
    }

    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }
}
