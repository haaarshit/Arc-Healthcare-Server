package com.example.HealthArc.Models;

import com.example.HealthArc.SupportClasses.*;
import com.example.HealthArc.SupportClasses.Doctor.Availability;
import com.example.HealthArc.SupportClasses.Doctor.DoctorPersonalInfo;
import com.example.HealthArc.SupportClasses.Doctor.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Doctor.Review;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctors")
@Getter
@Setter
public class Doctor {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String phone;
    private DoctorPersonalInfo personalInfo;
    private Address address;
    private ProfessionalInfo professionalInfo;
    private Availability availability;
    private int consultationFees;
    private List<String> languages;
    private List<Review> reviews;

}