package com.darwinruiz.airops.repositories;

import com.darwinruiz.airops.models.Pilot;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PilotRepository extends BaseRepository<Pilot, Long> {
    @Override
    protected Class<Pilot> entity() {
        return Pilot.class;
    }
}
