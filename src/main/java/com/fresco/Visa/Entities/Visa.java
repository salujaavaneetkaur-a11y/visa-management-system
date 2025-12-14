package com.fresco.Visa.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Visa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(unique=true)
    private String applicationId;
    private String country;
    private String visaType;
    private float duration;
    private String nationality;
    private String passportNumber;
    private String phoneNumber;
    private String status = "approval pending";
    
    @ManyToOne
    @JsonProperty("applicantId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private UserInfo applicant;

    public Visa(String applicationId, String country, String visaType, float duration, String nationality,
            String passportNumber, String phoneNumber) {
        super();
        this.applicationId = applicationId;
        this.country = country;
        this.visaType = visaType;
        this.duration = duration;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
    }
}