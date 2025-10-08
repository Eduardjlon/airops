package com.darwinruiz.airops.converters;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.repositories.AircraftRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "aircraftConverter", managed = true)
@ApplicationScoped
public class AircraftConverter implements Converter<Aircraft> {

    @Inject
    AircraftRepository repo;

    @Override
    public Aircraft getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return repo.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, Aircraft value) {
        return (value == null || value.getId() == null) ? "" : value.getId().toString();
    }
}
