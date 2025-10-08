package com.darwinruiz.airops.controllers;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.services.CatalogService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class AircraftBean implements Serializable {

    @Inject
    private CatalogService service;

    @Inject
    private Validator validator;

    private Aircraft selected;          // aeronave a crear o editar
    private boolean dialogVisible;      // controla visibilidad del diálogo
    private List<Aircraft> aircraftList; // lista de aeronaves para mostrar en la vista

    @PostConstruct
    public void init() {
        selected = new Aircraft();
        dialogVisible = false;
        loadAircraftList();
    }

    /** Carga la lista de aeronaves desde el servicio */
    public void loadAircraftList() {
        aircraftList = service.aircraft();
    }

    /** Devuelve la lista de aeronaves para la vista */
    public List<Aircraft> getAircraftList() {
        return aircraftList;
    }

    /** Prepara un nuevo registro */
    public void newItem() {
        clearFacesMessages();
        selected = new Aircraft();
        dialogVisible = true;
    }

    /** Edita un registro existente */
    public void edit(Aircraft a) {
        clearFacesMessages();
        this.selected = a;
        dialogVisible = true;
    }

    /** Guarda o actualiza la aeronave */
    public void save() {
        clearFacesMessages();

        // Validación con Bean Validation
        Set<ConstraintViolation<Aircraft>> violations = validator.validate(selected);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Aircraft> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                String label = getFieldLabel(field);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                label + ": " + message, null));
            }
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }

        // Guardar en la base de datos
        service.save(selected);

        // Actualizar lista para reflejar cambios en la vista
        loadAircraftList();

        dialogVisible = false;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aeronave guardada", "Operación exitosa"));

        selected = new Aircraft();
    }

    /** Elimina una aeronave */
    public void delete(Aircraft a) {
        service.delete(a);
        loadAircraftList();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aeronave eliminada", null));

        // Si la aeronave eliminada estaba seleccionada, reset
        if (selected != null && selected.equals(a)) {
            selected = new Aircraft();
            dialogVisible = false;
        }
    }

    /** Limpia los mensajes JSF */
    private void clearFacesMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        // Iterador correcto para eliminar mensajes
        Iterator<FacesMessage> it = ctx.getMessages();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    /** Etiquetas legibles para mensajes de validación */
    private String getFieldLabel(String fieldName) {
        Map<String, String> labels = new HashMap<>();
        labels.put("tailNumber", "Matrícula (Tail Number)");
        labels.put("model", "Modelo");
        labels.put("seats", "Asientos");
        labels.put("maxTakeoffWeightTons", "Peso máximo de despegue (toneladas)");

        return labels.getOrDefault(fieldName, fieldName);
    }

    // getters y setters
    public Aircraft getSelected() { return selected; }
    public void setSelected(Aircraft selected) { this.selected = selected; }

    public boolean isDialogVisible() { return dialogVisible; }
    public void setDialogVisible(boolean dialogVisible) { this.dialogVisible = dialogVisible; }
}
