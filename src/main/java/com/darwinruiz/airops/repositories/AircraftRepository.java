package com.darwinruiz.airops.repositories;


import com.darwinruiz.airops.models.Aircraft;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AircraftRepository extends BaseRepository<Aircraft, Long> {
    @Override
    protected Class<Aircraft> entity() {
        return Aircraft.class;
    }
}
