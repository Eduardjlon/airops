package com.darwinruiz.airops.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "[A-Z]{2}-[A-Z]{3}", message = "Ej. TG-ABC")
    @Column(unique = true)
    private String tailNumber;

    @NotBlank
    @Size(max = 60)
    private String model;

    @Min(2)
    @Max(350)
    private int seats;

    @NotNull
    @DecimalMin(value = "0.1", inclusive = true, message = "MTOW > 0")
    private Double maxTakeoffWeightTons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Double getMaxTakeoffWeightTons() {
        return maxTakeoffWeightTons;
    }

    public void setMaxTakeoffWeightTons(Double maxTakeoffWeightTons) {
        this.maxTakeoffWeightTons = maxTakeoffWeightTons;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Aircraft{");
        sb.append("id=").append(id);
        sb.append(", tailNumber='").append(tailNumber).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", seats=").append(seats);
        sb.append(", maxTakeoffWeightTons=").append(maxTakeoffWeightTons);
        sb.append('}');
        return sb.toString();
    }
}
