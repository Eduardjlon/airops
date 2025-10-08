package com.darwinruiz.airops.controllers;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.models.Flight;
import com.darwinruiz.airops.models.Pilot;
import com.darwinruiz.airops.services.CatalogService;
import com.darwinruiz.airops.services.FlightService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class FlightBean implements Serializable {

    @Inject
    private CatalogService catalog;

    @Inject
    private FlightService flights;

    @Inject
    private Validator validator;

    private Flight flight;             // vuelo a crear/editar
    private Flight selected;           // vuelo seleccionado
    private ScheduleModel schedule;    // modelo del calendario
    private boolean dialogVisible;     // controla visibilidad del diálogo

    @PostConstruct
    public void init() {
        flight = new Flight();
        schedule = new DefaultScheduleModel();
        dialogVisible = false;

        try {
            reloadSchedule();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al cargar vuelos: " + e.getMessage(), null));
        }
    }

    /** Nuevo vuelo */
    public void newFlight() {
        clearFacesMessages();
        this.flight = new Flight();
        this.selected = null;
        this.dialogVisible = true;
    }

    /** Selección de un evento en el calendario */
    public void onEventSelect(SelectEvent<ScheduleEvent<?>> e) {
        Object data = e.getObject().getData();
        if (data instanceof Flight f) {
            this.selected = f;
            this.flight = f;           // precarga para edición
            this.dialogVisible = true; // abre diálogo
        }
    }

    /** Guardar o actualizar vuelo */
    public void save() {
        clearFacesMessages();

        // Validación de Bean Validation
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Flight> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                String label = getFieldLabel(field);

                FacesContext.getCurrentInstance().addMessage("frmFlights:msgFlight",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                label + ": " + message, null));
            }
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }

        // Persistir o actualizar
        flights.save(flight);

        // Recargar calendario
        reloadSchedule();

        // Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Vuelo guardado", "Operación exitosa"));

        // Reset
        this.flight = new Flight();
        this.selected = null;
        this.dialogVisible = false;
    }

    /** Confirmar eliminación */
    public void confirmDelete() {
        if (selected != null) {
            flights.delete(selected);
            reloadSchedule();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Vuelo eliminado", "Operación exitosa"));

            this.selected = null;
            this.flight = new Flight();
            this.dialogVisible = false;
        }
    }

    /** Recarga el calendario (corregido para PrimeFaces) */
    public void reloadSchedule() {
        DefaultScheduleModel model = new DefaultScheduleModel();
        for (Flight f : flights.flights()) {
            DefaultScheduleEvent<Flight> ev = new DefaultScheduleEvent<>();
            ev.setTitle(f.getFlightNumber() + " • " + f.getOriginIata() + "→" + f.getDestinationIata());
            ev.setStartDate(f.getDeparture());
            ev.setEndDate(f.getArrival());
            ev.setData(f);
            model.addEvent(ev);
        }
        this.schedule = model;
    }

    /** Limpiar mensajes JSF */
    private void clearFacesMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        for (Iterator<FacesMessage> it = ctx.getMessages(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
    }

    /** Etiquetas de campos para mostrar en mensajes */
    private String getFieldLabel(String fieldName) {
        Map<String, String> labels = new HashMap<>();
        labels.put("flightNumber", "Número de vuelo");
        labels.put("originIata", "Origen (IATA)");
        labels.put("destinationIata", "Destino (IATA)");
        labels.put("departure", "Salida");
        labels.put("arrival", "Llegada");
        labels.put("passengers", "Pasajeros");
        labels.put("pilot", "Piloto");
        labels.put("aircraft", "Aeronave");
        return labels.getOrDefault(fieldName, fieldName);
    }

    // getters y setters
    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public Flight getSelected() { return selected; }
    public void setSelected(Flight selected) { this.selected = selected; }

    public ScheduleModel getSchedule() { return schedule; }

    public boolean isDialogVisible() { return dialogVisible; }
    public void setDialogVisible(boolean dialogVisible) { this.dialogVisible = dialogVisible; }

    public List<Pilot> getPilots() { return catalog.pilots(); }
    public List<Aircraft> getAircraft() { return catalog.aircraft(); }
    public List<Flight> getFlights() { return flights.flights(); }
}
