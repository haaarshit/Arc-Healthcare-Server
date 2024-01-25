package com.example.HealthArc.Models;

import com.example.HealthArc.SupportClasses.Patient.Allergy;
import com.example.HealthArc.SupportClasses.Patient.PatientPersonalInfo;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "patient")
public class Patient {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phone;

    private PatientPersonalInfo personalInfo;
    private List<Allergy> allergies;
    private String bloodType;
    private int height; // in centimeters
    private double weight; // in kilograms
    private List<String> dietPreferences;
    private Date createdOn;
    private Date updatedOn;
}
