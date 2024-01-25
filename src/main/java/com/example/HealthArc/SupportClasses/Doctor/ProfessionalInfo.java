package com.example.HealthArc.SupportClasses.Doctor;

import com.example.HealthArc.SupportClasses.WorkExperience;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessionalInfo {
    private String registrationNumber;

    private List<Qualification> qualifications;

    private List<String> specializations;

    private List<WorkExperience> workExperience;

//    private List<String> awards;
}

