package com.darwinruiz.airops.services;

import com.darwinruiz.airops.models.Flight;
import com.darwinruiz.airops.repositories.FlightRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FlightService {
    @Inject
    FlightRepository repo;

    public List<Flight> flights() {
        return repo.findAll();
    }

    public Flight save(Flight f) {
        return repo.save(f);
    }

    public void delete(Flight f) {
        repo.delete(f);
    }
}
