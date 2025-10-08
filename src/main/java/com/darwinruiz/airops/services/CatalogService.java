package com.darwinruiz.airops.services;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.models.Pilot;
import com.darwinruiz.airops.repositories.AircraftRepository;
import com.darwinruiz.airops.repositories.PilotRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CatalogService {
    @Inject
    PilotRepository pilotRepo;
    @Inject
    AircraftRepository aircraftRepo;

    public List<Pilot> pilots() {
        return pilotRepo.findAll();
    }

    public Pilot save(Pilot p) {
        return pilotRepo.save(p);
    }

    public void delete(Pilot p) {
        pilotRepo.delete(p);
    }

    public List<Aircraft> aircraft() {
        return aircraftRepo.findAll();
    }

    public Aircraft save(Aircraft a) {
        return aircraftRepo.save(a);
    }

    public void delete(Aircraft a) {
        aircraftRepo.delete(a);
    }
}
