package com.darwinruiz.airops.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número de vuelo con mensaje personalizado
    @NotBlank(message = "{flight.number.required}")
    @Pattern(regexp = "^[A-Z]{2}-\\d{3,4}$", message = "{flight.number.pattern}")
    @Column(unique = true)
    private String flightNumber;

    // IATA sin mensaje, usa los predeterminados
    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "{flight.iata.pattern}")
    private String originIata;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", message = "{flight.iata.pattern}")
    private String destinationIata;

    // Fechas sin mensajes específicos (usa los built-in del bundle)
    @NotNull
    @FutureOrPresent(message = "{flight.depart.future}")
    private LocalDateTime departure;

    @NotNull
    @Future(message = "{flight.arrive.future}")
    private LocalDateTime arrival;

    // Pasajeros con mensaje personalizado
    @Min(value = 1, message = "{flight.passengers.min}")
    private int passengers;

    // Relaciones: usan los mensajes por defecto ("Requerido")
    @ManyToOne(optional = false)
    @NotNull
    private Pilot pilot;

    @ManyToOne(optional = false)
    @NotNull
    private Aircraft aircraft;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
        validateChronology();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        validateChronology();
    }

    private void validateChronology() {
        if (departure != null && arrival != null && !arrival.isAfter(departure)) {
            throw new IllegalArgumentException("La llegada debe ser posterior a la salida.");
        }
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOriginIata() {
        return originIata;
    }

    public void setOriginIata(String originIata) {
        this.originIata = originIata;
    }

    public String getDestinationIata() {
        return destinationIata;
    }

    public void setDestinationIata(String destinationIata) {
        this.destinationIata = destinationIata;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", originIata='" + originIata + '\'' +
                ", destinationIata='" + destinationIata + '\'' +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", passengers=" + passengers +
                ", pilot=" + pilot +
                ", aircraft=" + aircraft +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}
