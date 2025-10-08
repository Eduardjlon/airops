package com.darwinruiz.airops.controllers;

import com.darwinruiz.airops.models.Pilot;
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
public class PilotBean implements Serializable {

    @Inject
    CatalogService service;

    @Inject
    private Validator validator;

    private Pilot selected;
    private boolean dialogVisible; // <-- controla el p:dialog

    @PostConstruct
    public void init() {
        selected = new Pilot();
        dialogVisible = false;
    }

    public List<Pilot> getList() {
        return service.pilots();
    }

    public void newPilot() {
        clearFacesMessages();
        selected = new Pilot();
        dialogVisible = true;
    }

    public void edit(Pilot p) {
        clearFacesMessages();
        this.selected = p;
        dialogVisible = true;
    }

    public void save() {
        Set<ConstraintViolation<Pilot>> violations = validator.validate(selected);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Pilot> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                String label = getFieldLabel(field);

                FacesContext.getCurrentInstance().addMessage("frmPilots:msgPilot",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                label + ": " + message, null));
            }

            // IMPORTANTE: Marcar que hubo errores de validación
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }

        service.save(selected);
        dialogVisible = false;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Piloto guardado", "Operación exitosa"));
        selected = new Pilot();
    }


    public void delete(Pilot p) {
        service.delete(p);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Piloto eliminado", null));
    }

    private void clearFacesMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        for (Iterator<FacesMessage> it = ctx.getMessages(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
    }

    private String getFieldLabel(String fieldName) {
        Map<String, String> labels = new HashMap<>();
        labels.put("fullName", "Nombre");
        labels.put("licenseCode", "Licencia");
        labels.put("email", "Email");
        labels.put("phone", "Teléfono");
        labels.put("birthDate", "Fecha de nacimiento");
        labels.put("flightHours", "Horas de vuelo");

        return labels.getOrDefault(fieldName, fieldName);
    }

    public Pilot getSelected() {
        return selected;
    }

    public void setSelected(Pilot selected) {
        this.selected = selected;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }


}
