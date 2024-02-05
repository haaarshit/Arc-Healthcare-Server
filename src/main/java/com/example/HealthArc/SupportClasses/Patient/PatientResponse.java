package com.example.HealthArc.SupportClasses.Patient;

import com.example.HealthArc.Models.Patient;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientResponse {
    private String id;
    private String email;
    private String phone;
    private String avatar;
    private PatientPersonalInfo personalInfo;
    private List<Allergy> allergies;
    private String bloodType;
    private int height; // in centimeters
    private double weight; // in kilograms
    private List<String> dietPreferences;
    private Date createdAt;
    private Date updatedAt;

    public PatientResponse returnResponse(Patient patient){
        return PatientResponse.builder().id(patient.getId())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .avatar(patient.getAvatar())
                .personalInfo(patient.getPersonalInfo())
                .allergies(patient.getAllergies())
                .bloodType(patient.getBloodType())
                .height(patient.getHeight())
                .weight(patient.getWeight())
                .dietPreferences(patient.getDietPreferences())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();

    }
}
