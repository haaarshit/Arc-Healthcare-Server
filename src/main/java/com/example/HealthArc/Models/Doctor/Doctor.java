package com.example.HealthArc.Models.Doctor;

import com.example.HealthArc.SupportClasses.Availability;
import com.example.HealthArc.SupportClasses.PersonalInfo;
import com.example.HealthArc.SupportClasses.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Review;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctors")
@Getter
@Setter
public class Doctor {
    @Id
    private String id;
    private PersonalInfo personalInfo;
    private ProfessionalInfo professionalInfo;
    private Availability availability;
    private int consultationFees;
    private List<String> languages;
    private List<Review> reviews;

}
