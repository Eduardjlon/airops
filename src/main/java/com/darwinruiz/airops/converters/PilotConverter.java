package com.darwinruiz.airops.converters;

import com.darwinruiz.airops.models.Pilot;
import com.darwinruiz.airops.repositories.PilotRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(value = "pilotConverter", managed = true)
@ApplicationScoped
public class PilotConverter implements Converter<Pilot> {

    @Inject
    PilotRepository repo;

    @Override
    public Pilot getAsObject(FacesContext ctx, UIComponent comp, String value) {
        // Si no hay selección, devuelve null para que 'required' funcione
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            return repo.find(id);          // devuelve la entidad por id
        } catch (NumberFormatException e) {
            return null;                   // fuerza validación si el value no es numérico
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, Pilot value) {
        return (value == null || value.getId() == null) ? "" : value.getId().toString();
    }
}
