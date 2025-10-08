package com.darwinruiz.airops.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
public class Pilot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60)
    private String fullName;

    @NotBlank
    @Pattern(regexp = "PIL-\\d{5}", message = "Formato PIL-12345")
    @Column(unique = true)
    private String licenseCode;

    @Email
    private String email;

    @Pattern(regexp = "^\\+?\\d{8,15}$", message = "Teléfono internacional 8-15 dígitos")
    private String phone;

    @Past
    private LocalDate birthDate;

    @Min(0)
    @Max(40000)
    private int flightHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getFlightHours() {
        return flightHours;
    }

    public void setFlightHours(int flightHours) {
        this.flightHours = flightHours;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Pilot{");
        sb.append("id=").append(id);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", licenseCode='").append(licenseCode).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", birthDate=").append(birthDate);
        sb.append(", flightHours=").append(flightHours);
        sb.append('}');
        return sb.toString();
    }
}
