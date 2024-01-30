package com.example.HealthArc.Repository.Patient;

import com.example.HealthArc.Models.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient,String> {
    Optional<Patient> findByEmail(String email);
}
