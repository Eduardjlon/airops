package com.darwinruiz.airops.repositories;


import com.darwinruiz.airops.models.Flight;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlightRepository extends BaseRepository<Flight, Long> {
    @Override
    protected Class<Flight> entity() {
        return Flight.class;
    }
}
