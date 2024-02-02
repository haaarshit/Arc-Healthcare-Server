package com.example.HealthArc.Services;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserType {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    public String getUserTypeFromDatabase(String userEmail) {
        Optional<Doctor> doctor = doctorRepository.findByEmail(userEmail);
        if (doctor.isPresent()) {
            return "doctor";
        }
        Optional<Patient> patient = patientRepository.findByEmail(userEmail);
        if(patient.isPresent()){
            return "patient";
        }
        return null; // User not found or no user type
    }
}
